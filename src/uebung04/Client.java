package uebung04;

import uebung01.Client.FibonacciProtocol;
import uebung01.Client.ProtocolException;
import uebung04.ClientImpl.ClientImpl;
import uebung04.ClientImpl.ClientReceiverThread;
import uebung04.ClientImpl.ServerConnection;
import uebung04.ServerImpl.Connection.ClientConnection;
import uebung04.util.JSONSerializer.ServerMessage;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;

public class Client {
    private static final int PORT = 5678;


    public static void main(String[] args) {
        final String hostName = "localhost";
        final int portNumber = PORT;
        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        int i = 0;

        try {
            Socket socket = new Socket(hostName, portNumber);
            ServerConnection conn = new ServerConnection(socket);
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            ArrayList<ServerMessage> smsg = new ArrayList<>();
            ClientImpl client = new ClientImpl(smsg);
            Thread thread = new Thread(new ClientReceiverThread(smsg, conn, client));
            thread.start();
            Thread.sleep(500L);
            while(true) {
                final String in = input.readLine();
                final String[] params = in.split(" ");
                if(params.length < 1) {
                    continue;
                } else if(params.length == 1) {
                    sendCommand(conn, i++, params[0], "");
                } else {
                    sendCommand(conn, i++, params[0], in.substring(params[0].length() + 1));
                }
                if(params[0].toLowerCase().equals("logout")) { // sub thread will die and this client will do so too
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendCommand(ServerConnection conn, int sequenceNumber, String command, String params) {
        String strpCommand = command.substring(1).toLowerCase();
        // this is bad, and you should feel bad:
        switch(strpCommand) {
            case "login":
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{params});
                break;
            case "logout":
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{});
                break;
            case "who":
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{});
                break;
            case "time":
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{});
                break;
            case "ls":
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{params});
                break;
            case "chat":
                String[] yawn = params.split(" ");
                if(yawn.length < 1) {
                    break;
                }
                String user = yawn[0];
                String msg = params.substring(user.length() + 1);
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{user, msg});
                break;
            case "notify":
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{params});
                break;
            case "note":
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{params});
                break;
            case "notes":
                conn.sendMessage(sequenceNumber, strpCommand, new String[]{});
                break;
            default:
                conn.sendMessage(sequenceNumber, "notify", new String[]{command + " " + params});
        }
    }
}
