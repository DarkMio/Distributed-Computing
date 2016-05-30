package uebung04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Mio on 30.05.2016.
 */

public class Server {

    public static final int PORT = 5678;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
            // Initiate conversation with client
            while (true) {
                String read = in.readLine();
                System.err.println(read);
                if(read != null){

                    String test = "String accepted";
                    outToClient.writeBytes(test + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
