import java.net.*;
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

 class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;
    // gui
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInputArea = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    // constructor
    public Client() {
        try {

            System.out.println("sending request to server");
            socket = new Socket("ip address", your portno);
            System.out.println("connection established");

            // creating object of BufferedReader
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // creating object of printwriter and to write data
            out = new PrintWriter(socket.getOutputStream());

            createGui();
            handleEvents();
            startReading();
            //startWriting();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
         //gui part
    private void handleEvents() {
        messageInputArea.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
            //System.out.println("key released" + e.getKeyCode());
                if(e.getKeyCode()==10){
                    System.out.println("you have pressed enter");

                    String contentToSend=messageInputArea.getText();
                    messageArea.append("Me:"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInputArea.setText("");
                    messageInputArea.requestFocus();
                }

            }

        });

    }
           //gui part
    public void createGui() {
        // creating gui
        this.setTitle("client messenger");
        this.setSize(500, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // coding for component

        heading.setFont(font);
        messageArea.setFont(font);
        messageInputArea.setFont(font);

        heading.setIcon(new ImageIcon("chat.jpg"));

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        messageArea.setEditable(false);

        // setting layout of frame
        this.setLayout(new BorderLayout());
        // adding components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jscrollPane=new JScrollPane(messageArea);
        this.add(jscrollPane, BorderLayout.CENTER);
        this.add(messageInputArea, BorderLayout.SOUTH);

        this.setVisible(true);

    }

    public void startReading() {
        // thread to read data

        Runnable r1 = () -> {                      //lambda expression
            System.out.println("Reader Started");
            try {
                while (true) {

                    // this msg will be comming from client
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("server has terminated the chat");
                        JOptionPane.showMessageDialog(null,"server has terminated the chat");
                        messageInputArea.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Server:" + msg);
                     messageArea.append("Server:" + msg + "\n");

                }
            } catch (Exception e) {
                System.out.println("connection closed");
            }

        };

        new Thread(r1).start(); // starting thread

    }

 //This writing code was just for understanding writing work is done in gui part
    // public void startWriting() {

    //     // thread to write data and can send to client

    //     Runnable r2 = () -> {
    //         System.out.println("writer started...");
    //         try {
    //             while (true && !socket.isClosed()) {

    //                 // reading the data from user through console to send to client
    //                 BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

    //                 String content = br1.readLine();

    //                 System.out.println(content);
    //                 out.flush();

    //                 if (content.equals("exit")) {
    //                     socket.close();
    //                     break;
    //                 }

    //             }
    //             System.out.println("connection closed");
    //         } catch (Exception e) {
    //             System.out.println("connection closed");
    //         }

    //     };

    //     new Thread(r2).start();

    // }

    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();

    }

}
