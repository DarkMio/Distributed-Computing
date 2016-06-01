package uebung04;

import uebung01.Client.FibonacciProtocol;
import uebung01.Client.ProtocolException;
import uebung04.ClientImpl.ClientImpl;
import uebung04.ClientImpl.ClientReceiverThread;
import uebung04.ClientImpl.ClientSenderThread;
import uebung04.ClientImpl.ServerConnection;
import uebung04.ServerImpl.Connection.ClientConnection;
import uebung04.util.JSONSerializer.ServerMessage;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.UUID;

public class Client {
    private static final int PORT = 5678;
    private static final String HOST_NAME = "localhost";

    public static void main(String[] args) {

        try {
            Socket socket = new Socket(HOST_NAME, PORT);
            ServerConnection conn = new ServerConnection(socket);
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            ArrayList<ServerMessage> smsg = new ArrayList<>();
            ClientImpl client = new ClientImpl(smsg, conn.getSentMessages());
            // if you're too lazy to write non-blocking processes, write it multi threaded
            Thread serverInput = new Thread(new ClientReceiverThread(smsg, conn, client));
            serverInput.start();
            Thread userInput = new Thread(new ClientSenderThread(conn));
            userInput.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}