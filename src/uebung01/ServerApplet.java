package uebung01;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerApplet {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5678);
            Socket clientSocket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

            int input;

            // Initiate conversation with client
            System.out.println("Connected to FAAS (Fibonacci As A Service) | exit or quit to disconnect.");
            while (true) {
                input = dis.readInt();
                if(input == -1) {
                    break;
                }
                int val = fibonacci(input);
                dos.writeInt(val);
            }
            serverSocket.close();
        } catch (SocketException e) {
            System.err.println("Socket is forcibly closed. Exiting...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int fibonacci(final int input) {
        if(input <= 0) {
            return 0;
        } else if(input == 1) {
            return 1;
        } else {
            return fibonacci(input - 2) + fibonacci(input - 1);
        }
    }
}
