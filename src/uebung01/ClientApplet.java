package uebung01;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientApplet {

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
                    int data = 0;
                    while(true) {
                        System.out.print("Input> ");
                        fromUser = input.readLine();
                        data = FibonacciProtocol.processInput(fromUser);
                        if(data >= 0) {
                            break;
                        }
                    }
                    dos.write(data);
                    int fromServerResponse = dis.read();
                    System.out.println(fromServerResponse);
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
