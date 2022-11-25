import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    // Creating a new ArrayList of ClientHandler objects.
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    //when client send msg -> we will loop this array and send msg to each client

    private Socket socket; //socket to connect client and server 
    private BufferedReader readData;
    private BufferedWriter writeData;
    private String clientName;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket;
            // 2 type: byte stream and character stream
            // we use character strem -> send msg
            this.writeData = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.readData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientName = readData.readLine(); // enter

            // add client to array
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientName + " has joined");
        } catch (IOException e) {
            closeEverything(socket, readData, writeData);
        }
    }

    private void closeEverything(Socket socket2, BufferedReader readData2, BufferedWriter writeData2) {
        //close down connection
        ClientOut();
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

    private void broadcastMessage(String string) {
        for(ClientHandler clientHandler : clientHandlers){
            try{
                if (!clientHandler.clientName.equals(clientName)){
                    clientHandler.writeData.write(string); // send msg to other
                    clientHandler.writeData.newLine();
                    clientHandler.writeData.flush();
                }
            } catch (IOException e){
                closeEverything(socket, readData, writeData);
            }   
        }
    }

    public void ClientOut(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientName + " has left");
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        // run on a separate thread
     
        String ClientMsg;
        while(socket.isConnected()){
            try{
                ClientMsg = readData.readLine();
                broadcastMessage(ClientMsg);
            } catch (IOException e) {
                closeEverything(socket, readData, writeData);
                break; // client disconnect
            }
        }     
    }
    

}
