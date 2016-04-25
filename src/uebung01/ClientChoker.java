package uebung01;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Mio on 25.04.2016.
 */
public class ClientChoker {

    public static void main(String[] args) {
        String hostName;
        int portNumber;
        if(args.length >=2){
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        } else if (args.length == 1){
            hostName = args[0];
            portNumber = 5678;
        } else {
            System.err.println("No IP in args");
            System.exit(-1);
            hostName = "";
            portNumber = 0;
        }
        try {
            Socket kkSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            DataInputStream dis = new DataInputStream(kkSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(kkSocket.getOutputStream());
            while (true) {

                String fromUser;
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Input> ");
                fromUser = input.readLine();
                if(fromUser != null) {
                    out.println("Choke the server to its sudden death.");

                    for(int i = 0; i < 150; i++) {
                        System.out.println(in.read());
                    }
                } else {
                    break;
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
