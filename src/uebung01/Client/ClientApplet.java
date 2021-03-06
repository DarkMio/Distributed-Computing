package uebung01.Client;

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
            hostName = "";  //Makes inspection & compiler warnings happy
            portNumber = 0; // about uninitialized variables down below
        }
        try {
            Socket socket = new Socket(hostName, portNumber);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String fromUser;
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Input> ");
                fromUser = input.readLine();
                if (fromUser != null) {
                    int data = FibonacciProtocol.processInput(fromUser);
                    if (data == Integer.MIN_VALUE) {
                        break;
                    } else if (data == Integer.MAX_VALUE) {
                        continue;
                    }
                    dos.writeInt(data);
                    int fromServerResponse = dis.readInt();
                    output(fromServerResponse);
                } else {
                    break;
                }
            }
            socket.close();
        } catch (ProtocolException e) {
            System.err.println(e.getMessage());
        } catch (SocketException e) {
            System.err.println("ServerImpl forcibly closed the connection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void output(int data) throws ProtocolException {
        System.out.print("ServerImpl> ");
        if(data < 0) {
            throw new ProtocolException(data);
        } else {
            System.out.println("Result is " + data);
        }
    }
}
