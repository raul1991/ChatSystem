/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import commons.Constants;
import commons.Messages;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Member;

/**
 *
 * @author user
 */
public class MultiChat implements Constants {

    private static HashMap<Member, PrintWriter> clientoutputstreams;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        clientoutputstreams = new HashMap<Member, PrintWriter>();
        ServerSocket server_sock = new ServerSocket(server_port);

        while (true) {
            Socket clients = server_sock.accept();
            Thread t = new Thread(new multiclient(clients));
            t.start();

        }
    }

    public static class multiclient implements Runnable {

        private Socket connection;
        private InputStream incoming_connection_stream;
        private BufferedReader reader;
        private Calendar c;
        private SimpleDateFormat df;
        private PrintWriter pw;
        private static Random randomColor=new Random( );
        
        private int colorMaker(){
            return (randomColor.nextInt(256));
        }
        public multiclient(Socket client) throws IOException {

            this.connection = client;
            incoming_connection_stream = client.getInputStream();
            reader = new BufferedReader(new InputStreamReader(incoming_connection_stream));
            pw = new PrintWriter(connection.getOutputStream());

            c = Calendar.getInstance();
            df = new SimpleDateFormat("hh:mm:ss");
        }

        @Override
        public void run() {
            try {
                String line = null;


                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(ACTION_SUFFIX_JUST_JOINED)) {
                        /**
                         * Incoming packet: JUST_JOINED#userproperties
                         * user properties : nickname/time/address/status/color
                         */
                        String token = line.split(ACTION_SEPARATOR)[1];
                        String member_details[] = token.split(ACTION_SEPARATOR_USER_LIST);
                        
                        Member member = new Member();
                        member.setNickname(member_details[0]);
                        member.setTime(member_details[1]);
                        member.setAddress(member_details[2]);
                        member.setStatus(member_details[3]);
                        member.setColor(Integer.parseInt(member_details[4]));
                        clientoutputstreams.put(member, pw);
                        StringBuilder users = new StringBuilder();
                        for (Member m : clientoutputstreams.keySet()) {
                            users.append(m.getNickname()).append(";").append(m.getAddress()).append(";").append(m.getStatus()).append(";").append(m.getColor()).append(";").append(ACTION_SEPARATOR_USER_LIST);
                        }
                        telltoNewComer(member, users.toString());
                        users = null;
                        telltoOthersaboutothers(member);
                    } else if (line.startsWith(ACTION_SUFFIX_MESSAGE)) {

                        telltoOthers(ACTION_SUFFIX_MESSAGE+ACTION_SEPARATOR+ line.split(ACTION_SEPARATOR)[1], Messages.MESSAGE);
                    } else if (line.startsWith(ACTION_SUFFIX_USER_EXITED)) {

                        removeUser(line.split(ACTION_SEPARATOR)[1]);
                    } else if (line.startsWith(ACTION_SUFFIX_IS_AVAILABLE)) {
                        String username = line.split(ACTION_SEPARATOR)[1];
                        if (checkifUserExists(username)) {
                            pw.println(ACTION_SUFFIX_IS_AVAILABLE+ACTION_SEPARATOR + true);
                            
                        } else {
                            pw.println(ACTION_SUFFIX_IS_AVAILABLE+ACTION_SEPARATOR+ false);
                        }
                        pw.flush();
                    }else if(line.startsWith(ACTION_SUFFIX_STATUS_CHANGE)){    
                        /**
                         * Packet for status_change: STATUS_CHANGE#username:newStatus
                         */
                        String newStatus=line.split(ACTION_SEPARATOR_USER_JOINED_TIME)[1];
                        String username=line.split(ACTION_SEPARATOR)[1].split(ACTION_SEPARATOR_USER_JOINED_TIME)[0];
                        telltoOthers(ACTION_SUFFIX_STATUS_CHANGE+ACTION_SEPARATOR+username+ACTION_SEPARATOR_USER_JOINED_TIME+newStatus, Messages.STATUS_CHANGE);
                        
                    }
                    


                }
            } catch (IOException ex) {
                Logger.getLogger(multiclient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        /**
         * 
         *
         * @param Groupmessage
         * @param msg_type
         */
        public void telltoOthers(String Groupmessage, Messages msg_type) {
            for (PrintWriter printWriter : clientoutputstreams.values()) {
                if (msg_type == Messages.JUST_JOINED) {
                    printWriter.println(ACTION_SUFFIX_JUST_JOINED+ACTION_SEPARATOR + Groupmessage);
                    printWriter.flush();
                } else if (msg_type == Messages.MESSAGE) {
                    printWriter.println(Groupmessage);
                    printWriter.flush();
                } else if (msg_type == Messages.USER_EXITED) {
                    printWriter.println(Groupmessage);
                    printWriter.flush();
                } else if (msg_type == Messages.GIVE_ME_LISTS) {
                    printWriter.println(Groupmessage);
                    printWriter.flush();
                }else if (msg_type == Messages.STATUS_CHANGE) {
                    printWriter.println(Groupmessage);
                    printWriter.flush();
                }
                

            }

        }

        private void telltoOthersaboutothers(Member member) {
            /**
             * Packet to send to other about the new joinee.
             * JUST_JOINED#userproperties
             * userproperties : nickname/time/address/status/color
             */
//            String user = member.getNickname();
            telltoOthers(member.toString(), Messages.JUST_JOINED);
        }

        /**
         * pass in the name of the user to remove/sign out.
         *
         * @param user
         */
        private void removeUser(String user) {
            //notify others about his signing out.
            Member m = null;
            for (Map.Entry<Member, PrintWriter> entry : clientoutputstreams.entrySet()) {
                Member member = entry.getKey();
                if (member.getNickname().equals(user)) {
                    m = member;
                    telltoOthers(ACTION_SUFFIX_USER_EXITED+ACTION_SEPARATOR + member.getNickname(), Messages.USER_EXITED);

                }
            }
            clientoutputstreams.remove(m);
        }

        private boolean checkifUserExists(String userToSearch) {
            for (Member member : clientoutputstreams.keySet()) {
                if (member.getNickname().equals(userToSearch)) {
                    return true;
                }
            }
            return false;
        }

        private void telltoNewComer(Member newMember, String prevUsers) {
            PrintWriter printWriter = null;
            String s = newMember.getNickname();
            for (Map.Entry<Member, PrintWriter> entry : clientoutputstreams.entrySet()) {
                Member member = entry.getKey();
                if (s.equals(member.getNickname())) {
                    printWriter = entry.getValue();
                    break;
                }

            }
            
            printWriter.println(ACTION_SUFFIX_GIVE_ME_LISTS+ACTION_SEPARATOR + prevUsers);
            printWriter.flush();
        }
    }
}