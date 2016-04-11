package uebung01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Mio on 11.04.2016.
 */
public class ServerApplet {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;

            // Initiate conversation with client
            FibonacciProtocol fib = new FibonacciProtocol();
            out.println("Connected to FAAS (Fibonacci As A Service)");
            while ((inputLine = in.readLine()) != null) {
                try {
                    int value = Integer.parseInt(inputLine);
                    outputLine = "Fibonacci of " + value + " is: " + fib.processInput(value);
                    out.println(outputLine);
                } catch (NumberFormatException e) {
                    out.println("Invalid number, disconnecting...");
                    break;
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
