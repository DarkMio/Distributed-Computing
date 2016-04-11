package uebung01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerApplet {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;

            // Initiate conversation with client
            FibonacciProtocol fib = new FibonacciProtocol();
            out.println("Connected to FAAS (Fibonacci As A Service) | exit or quit to disconnect.");
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("exit") || inputLine.contains("quit")) {
                    out.println("Disconnecting...");
                    break;
                }
                out.println(fib.processInput(inputLine));
            }
            serverSocket.close();
        } catch (SocketException e) {
            System.err.println("Socket is forcibly closed. Exiting...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
