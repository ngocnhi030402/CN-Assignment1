import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    private Socket socket; //socket to connect client and server 
    private BufferedReader readData;
    private BufferedWriter writeData;
    private String userName;

    public Client(Socket socket, String userName) {
        try{
            this.socket = socket;
            this.writeData = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.readData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;

        } catch (IOException e) {
            closeEverything(socket, readData, writeData);
        }
    }

    private void closeEverything(Socket socket2, BufferedReader readData2, BufferedWriter writeData2) {
        try{
            if(readData2 != null){
                readData2.close();
            }
            if(writeData2 != null){
                writeData2.close();
            }
            if(socket2 != null){
                socket2.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sendMsg(){
        try{
            writeData.write(userName);
            writeData.newLine();
            writeData.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String message = scanner.nextLine();
                writeData.write(userName + ": " + message);
                writeData.newLine();
                writeData.flush();
            }
        } catch (IOException e){
            closeEverything(socket, readData, writeData);
        }
    }

    public void listenForMessage(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String groupChat;

                while(socket.isConnected()){
                    try{
                        groupChat = readData.readLine();
                        System.out.println(groupChat);
                    } catch (IOException e){
                        closeEverything(socket, readData, writeData);
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws UnknownHostException, IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, name);
        client.listenForMessage();
        client.sendMsg();
    }
}
