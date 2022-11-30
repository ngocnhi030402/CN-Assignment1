import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;  


public class    Server {
    JTextArea text = new JTextArea(100, 50);
    JScrollPane log;
    JPanel notify = new JPanel();
    BufferedWriter writer;
    BufferedReader reader;
    HashMap<String,ClientHandler> onl = new HashMap<>();

    public HashMap<String, ClientHandler> getOnline() {
        return onl;
    }

    
    public JPanel getNotify(){
        return this.notify;
    }

    public JTextArea getText(){
        return this.text;
    }

    private class CreateClient implements Runnable{
        Socket ss;
        Server log;

        public CreateClient(Socket ss, Server log) {
            this.ss = ss;
            this.log = log;
        }

        // @Override
        public void run() {
            try {
                new ClientHandler(ss, log);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //connect server
    public void connect() {
        try {
            ServerSocket socket = new ServerSocket(3200);
            do {
                // if no client connected
                text.append("Waiting for a client... \n");
                // if client connected
                Socket serverSocket = socket.accept(); //synchronous
                text.append("A new client is coming!\n");

                // create thread -> thread share memory 
                // when launch an executable, it is running in a thread within a process
                Thread thread = new Thread(new CreateClient(serverSocket, this));
                thread.start();

            } while (true);
        } catch (IOException e) {

            // text.append("There are some error\n");

        } finally {
            try {
                writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Server() {
        JFrame frame = new JFrame("Server"); // set title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close -> exit program
        frame.setPreferredSize(new Dimension(500, 400));
        frame.setLayout(new BorderLayout());
        
        log = setJPanel();
        frame.add(log);
        frame.pack();
        
        frame.setVisible(true);

        connect();
    }

    public JScrollPane setJPanel() {
        text.setEditable(false);
        JScrollPane log = new JScrollPane(text);
        return log;
    }



    public static void main(String args[]) {
        new Server();
    }
}
