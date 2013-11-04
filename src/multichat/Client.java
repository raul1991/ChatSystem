/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multichat;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalIconFactory;
import model.Member;

/**
 *
 * @author user
 */
public class Client extends javax.swing.JFrame implements ListSelectionListener, constants {

    private DateFormat df;
    private String time;
    private final Member member;

    public class TimerThread implements Runnable {

        public TimerThread() {
            //c=Calendar.getInstance();
            df = new SimpleDateFormat("hh:mm:ss");

        }

        @Override
        public void run() {
            while (true) {
                time = df.format(System.currentTimeMillis());

                timespent.setText("<html><body><b><font color=#0000CC>" + time + "</font></b></body></html>");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    private Socket client_sock;
    private InputStream incoming_stream;
    private OutputStream outgoing_stream;
    private BufferedReader reader_buffer;
    private PrintWriter writer;

    /**
     * Creates new form Client
     */
    public Client(Socket s, Member m) throws UnknownHostException, IOException {
        initComponents();
        this.member = m;
        iconlist.addListSelectionListener(this);
//        setupimages();
        username.setFont(new Font("sans", Font.BOLD, 20));
        username.setText(m.getNickname());
        client_sock = s;
        incoming_stream = client_sock.getInputStream();
        reader_buffer = new BufferedReader(new InputStreamReader(incoming_stream));
        outgoing_stream = client_sock.getOutputStream();
        writer = new PrintWriter(outgoing_stream);
        
        writer.println("USER_JOINED#" + m);
        writer.flush();
        
        Thread incoming_reader = new Thread(new MessageReader(m));
        Thread timer = new Thread(new TimerThread());
        incoming_reader.start();
        timer.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        timespent = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ChatWindow = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        Send = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ClientMessage = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        userlist = new javax.swing.JList();
        jScrollPane5 = new javax.swing.JScrollPane();
        iconlist = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setToolTipText("username\n");
        jPanel1.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N

        jLabel2.setText("Username -");

        jLabel1.setText("Time Spent -");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addGap(84, 84, 84)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timespent, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(timespent, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(21, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)))
                .addContainerGap())
        );

        ChatWindow.setColumns(20);
        ChatWindow.setEditable(false);
        ChatWindow.setRows(5);
        ChatWindow.setWrapStyleWord(true);
        jScrollPane2.setViewportView(ChatWindow);

        Send.setText("send");
        Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendActionPerformed(evt);
            }
        });

        ClientMessage.setColumns(20);
        ClientMessage.setRows(5);
        ClientMessage.setToolTipText("Message Box");
        ClientMessage.setWrapStyleWord(true);
        jScrollPane1.setViewportView(ClientMessage);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Send))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(Send, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.setBackground(new java.awt.Color(153, 51, 255));
        jTabbedPane2.setForeground(javax.swing.UIManager.getDefaults().getColor("Button.disabledForeground"));

        userlist.setModel(iconlist.getModel());
        jScrollPane4.setViewportView(userlist);

        jTabbedPane2.addTab("Users", jScrollPane4);

        iconlist.setModel(iconlist.getModel());
        jScrollPane5.setViewportView(iconlist);

        jTabbedPane2.addTab("tab2", jScrollPane5);

        jScrollPane3.setViewportView(jTabbedPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendActionPerformed
        String message = ClientMessage.getText();

        ClientMessage.setText("");
        ChatWindow.setForeground(Color.black);
        ChatWindow.setFont(new Font("monospace", Font.BOLD, 14));
        writer.println("MESSAGE#" + member.getNickname() + ":" + message);
        writer.flush();
    }//GEN-LAST:event_SendActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea ChatWindow;
    private javax.swing.JTextArea ClientMessage;
    private javax.swing.JButton Send;
    private javax.swing.JList iconlist;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel timespent;
    private javax.swing.JList userlist;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables

    private void setupimages() {
        //  URL url=null;
        DefaultListModel icons_model = new DefaultListModel();
        //  Map<Object, Icon> icons = new HashMap<Object, Icon>();
        for (int i = 1; i < 33; i++) {
            JLabel label = new JLabel("", JLabel.CENTER);
            java.net.URL url = getClass().getClassLoader().getResource("image-" + i + ".gif");
            if (url != null) {
                label.setIcon(MetalIconFactory.getFileChooserHomeFolderIcon());
                icons_model.addElement(label.createImage(100, 100));
                iconlist.setModel(icons_model);
            } else {
                System.err.println("NULL image");
            }


        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Clicked:" + e.getSource());
    }

    public class MessageReader implements Runnable {

        private String tokens[];
        private DefaultListModel listmodel = new DefaultListModel();
//        private String nickname;
        private Member m;
//        private String name;

        public MessageReader(Member m) {
            this.m = m;
        }

        @Override
        public void run() {
            try {
                String line = "";
                while ((line = reader_buffer.readLine()) != null) {
                    System.out.println(line + "--->");
                    if (line.contains("just joined")) {
                        tokens = line.split("-");
                        if (!tokens[0].equals(this.m.getNickname())) {
                            adduser(tokens[0]);
                            System.out.println("read" + line);
                            ChatWindow.setForeground(Color.blue);
                            ChatWindow.setFont(new Font("monospace", Font.ITALIC, 14));
                            ChatWindow.append("\n" + line);
                        } else {
                            System.out.println("it's you...");
                        }
                    }
                    if(line.startsWith("LIST")){
                        tokens=line.split("#");
                        ChatWindow.setForeground(Color.BLACK);
                        ChatWindow.setFont(new Font("monospace", Font.ITALIC, 14));

                        for(String users:tokens[1].split("/")){
                            if(!users.equals(this.m.getNickname())){
                                ChatWindow.append("\n" + users +"is available...");
                                adduser(users);
                            }
                        }
                    }
                    if(line.startsWith("INFO")){
                        ChatWindow.setForeground(Color.red);
                        ChatWindow.append("\n" + line);
                    }
                    
                    if(line.startsWith("MESSAGE")){
                        ChatWindow.setFont(new Font("monospace", Font.ITALIC, 14));
                        ChatWindow.append("\n"+line.split("#")[1]);
                    }
                }

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void adduser(String username) {
//            System.out.println(username + "split");

            listmodel.addElement(new JLabel("<html><body><b><font color=#AA66CC>" + username + "</font></b></body></html>").getText());
            userlist.setBackground(Color.ORANGE);
            userlist.setSelectionBackground(Color.lightGray);

            userlist.setModel(listmodel);

        }
    }
}
