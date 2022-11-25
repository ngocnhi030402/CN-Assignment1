import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.io.IOException;  
import java.net.*; 

public class Server {
    private ServerSocket serverSocket;

    // alt + ins constructor
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // keep server running
    public void startServer() {

        try {
            while (!serverSocket.isClosed()) {
                // waiting client connected
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");

                // making communication with Client
               ClientHandler clientHandler = new ClientHandler(socket);
                // we didnt spawn a new thread to hanlde the connection
                // with each new client, means 1 client at a time

                // threads share a memory space
                // when launch an executable, it is running in a thread within a process
               Thread thread = new Thread(clientHandler);
               thread.start();
            }
        } catch (IOException e) {

        }
    }

    // public void printStackTrace(PrintStream s);

    // to avoid nested try catch
    public void closeServerSocket() {
        // check not null
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // main
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}
