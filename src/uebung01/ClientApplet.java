package uebung01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientApplet {

    public static void main(String[] args) {
        String hostName = "127.0.0.1";
        int portNumber = 6789;

        try {
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Exiting...")) {
                    break;
                }
                String fromUser;
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Input> ");
                fromUser = input.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
            kkSocket.close();
        } catch (SocketException e) {
            System.err.println("Server forcibly closed the connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
