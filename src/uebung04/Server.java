package uebung04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Mio on 30.05.2016.
 */

public class Server implements Runnable{


    public static final int PORT = 5678;

    private Socket clientSocket;
    private int ID;
    public static void main(String[] args) {
        int count = 0;
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            // Initiate conversation with client
            while (true) {
                Socket clientSocket = serverSocket.accept();
                if(count > 5) {
                    break;
                }
                Runnable runnable = new Server(clientSocket, ++count);
                Thread thread = new Thread(runnable);
                thread.start();
                thread.run();
              /*  BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                String read = in.readLine();
                System.err.println(read);
                if(read != null){

                    String test = "String accepted";
                    outToClient.writeBytes(test + "\n");
                } */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Server(Socket s, int i){
        this.clientSocket = s;
        this.ID = i;
    }

    public void run(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
            String read = in.readLine();
            System.err.println(read);
            if(read != null){

                String test = "String accepted";
                outToClient.writeBytes(test + "\n");
            }
            try {
                Thread.sleep(10000);
            }
            catch (Exception e) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
