package uebung01;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientChoker {

    public static void main(String[] args) {
        String hostName = "127.0.0.1";
        int portNumber = 5678;
        try {
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            DataInputStream dis = new DataInputStream(kkSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(kkSocket.getOutputStream());
            while (true) {
                out.println("Choke the server to its sudden death.");
                for(int i = 0; i < 150; i++) {
                    System.out.println(dis.readInt());
                }
            }
        } catch (SocketException e) {
            System.err.println("Server forcibly closed the connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
