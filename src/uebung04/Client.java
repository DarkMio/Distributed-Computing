package uebung04;

import uebung01.Client.FibonacciProtocol;
import uebung01.Client.ProtocolException;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {
        String hostName;
        int portNumber;
        if (args.length >= 2) {
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        } else if (args.length == 1) {
            hostName = args[0];
            portNumber = 5678;
        } else {
            System.err.println("No IP in args");
            System.exit(-1);
            hostName = "";  //Makes inspection & compiler warnings happy
            portNumber = 0; // about uninitialized variables down below
        }
        try {
            Socket socket = new Socket(hostName, portNumber);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String fromUser;
                System.out.print("Input> ");
                fromUser = inFromUser.readLine();
                if (fromUser != null) {
                    outToServer.writeBytes(fromUser + "\n");
                    String test2 = inFromServer.readLine();
                    System.out.println(test2);
                } else {
                    break;
                }
                socket.close();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
