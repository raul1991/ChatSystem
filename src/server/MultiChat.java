/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import commons.Messages;
import commons.constants;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Member;

/**
 *
 * @author user
 */
public class MultiChat implements constants {

    private static HashMap<Member, PrintWriter> clientoutputstreams;
//    private static ArrayList<Member> $listofusers;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        clientoutputstreams = new HashMap<Member, PrintWriter>();
//        $listofusers = new ArrayList<Member>();
        ServerSocket server_sock = new ServerSocket(server_port);
        int i = 0;

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

        public multiclient(Socket client) throws IOException {

            this.connection = client;
            incoming_connection_stream = client.getInputStream();
            reader = new BufferedReader(new InputStreamReader(incoming_connection_stream));
            c = Calendar.getInstance();
            df = new SimpleDateFormat("hh:mm:ss");
        }

        @Override
        public void run() {
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {

                    if (line.startsWith("JUST_JOINED")) {
                        String token = line.split("#")[1];
                        String member_details[] = token.split("/");
                        Member member = new Member();
                        member.setNickname(member_details[0]);
                        member.setTime(member_details[1]);
//                        $listofusers.add(member);

                        PrintWriter pw = new PrintWriter(connection.getOutputStream());
                        clientoutputstreams.put(member, pw);
//                        if (!$listofusers.isEmpty()) {

                        StringBuilder users = new StringBuilder();
                        for (Member m : clientoutputstreams.keySet()) {
                            users.append(m.getNickname()).append("/");
                        }
                        pw.println("LIST#" + users.toString());
                        pw.flush();
                        users = null;
//                        } else {
//                            pw.println("INFO#No user currently available.");
//                            pw.flush();
//                        }
//                        clientoutputstreams.put(member.getNickname(), pw);
                        telltoOthersaboutothers(member);
                    }
                    if (line.startsWith("MESSAGE")) {

                        telltoOthers("MESSAGE#"+line.split("#")[1], Messages.MESSAGE);
                    }

                    if (line.startsWith("USER_EXITED")) {

                        removeUser(line.split("#")[1]);
                    }


                }
            } catch (IOException ex) {
                Logger.getLogger(multiclient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        /**
         * Redundant if else exists.Do check.
         * @param Groupmessage
         * @param msg_type 
         */
        public void telltoOthers(String Groupmessage, Messages msg_type) {
//            Iterator it = clientoutputstreams.values();
            for (PrintWriter printWriter : clientoutputstreams.values()) {
//                PrintWriter wr = (PrintWriter) it.next();
                if (msg_type == Messages.JUST_JOINED) {
                    printWriter.println(Groupmessage);
                    printWriter.flush();
                } else if (msg_type == Messages.MESSAGE) {
                    printWriter.println(Groupmessage);
                    printWriter.flush();
                }else if (msg_type==Messages.USER_EXITED){
                    printWriter.println(Groupmessage);
                    printWriter.flush();
                }

            }

        }

        private void telltoOthersaboutothers(Member member) {
            String groupmessage = "just joined the chat application.";
            String user = member.getNickname();
            telltoOthers("JUST_JOINED#" + user + "/" + (member.getTime()), Messages.JUST_JOINED);
        }

        /**
         * pass in the name of the user to remove/sign out.
         *
         * @param user
         */
        private void removeUser(String user) {

            //notify others about his signing out.
            
            for (Map.Entry<Member, PrintWriter> entry : clientoutputstreams.entrySet()) {
                Member member = entry.getKey();
                PrintWriter printWriter = entry.getValue();
                if (member.getNickname().equals(user)) {
                    clientoutputstreams.remove(member);
                    telltoOthers("USER_EXITED#"+member.getNickname(), Messages.USER_EXITED);
                    
                }
            }
        }
    }
}