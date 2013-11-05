/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multichat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Member;

/**
 *
 * @author user
 */
public class MultiChat implements constants {

    private static ArrayList<PrintWriter> clientoutputstreams;
    private static ArrayList<Member> $listofusers;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        clientoutputstreams = new ArrayList();
        $listofusers = new ArrayList<Member>();
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
                  
                    if (line.startsWith("USER_JOINED")) {
                        String token = line.split("#")[1];
                        String member_details[] = token.split("/");
                        Member member = new Member();
                        member.setNickname(member_details[0]);
                        member.setTime(member_details[1]);
                        $listofusers.add(member);

                        PrintWriter pw = new PrintWriter(connection.getOutputStream());
                        if (!$listofusers.isEmpty()) {

                            StringBuilder users = new StringBuilder();
                            for (Member m : $listofusers) {
                                users.append(m.getNickname()).append("/");
                            }
                            pw.println("LIST#" + users.toString());
                            pw.flush();
                        } else {
                            pw.println("INFO#No user currently available.");
                            pw.flush();
                        }
                        clientoutputstreams.add(pw);
                        telltoOthersaboutothers(member);
                    }
                    if (line.startsWith("MESSAGE")) {

                        telltoOthers(line.split("#")[1], Messages.MESSAGE);
                    }

                    if (line.startsWith("USER_EXITED")) {
                        
                        removeUser(line.split("#")[1]);
                    }


                }
            } catch (IOException ex) {
                Logger.getLogger(multiclient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public void telltoOthers(String Groupmessage, Messages msg_type) {
            Iterator it = clientoutputstreams.iterator();
            while (it.hasNext()) {
                PrintWriter wr = (PrintWriter) it.next();
                if (msg_type == Messages.JUST_JOINED) {
                    wr.println("" + Groupmessage);
                    wr.flush();
                } else if (msg_type == Messages.MESSAGE) {
                    wr.println("MESSAGE#" + Groupmessage);
                    wr.flush();
                }

            }

        }

        private void telltoOthersaboutothers(Member member) {
            String groupmessage = "just joined the chat application.";
            String user = member.getNickname();
            telltoOthers(user + "-" + groupmessage + " Time:" + (member.getTime()), Messages.JUST_JOINED);
        }

        /**
         * pass in the name of the user to remove/sign out.
         *
         * @param user
         */
        private void removeUser(String user) {
            $listofusers.remove(user);
            //notify others about his signing out.
            for (PrintWriter printWriter : clientoutputstreams) {
                printWriter.println("USER_EXITED#" + user);
                printWriter.flush();
            }
        }
    }
}
