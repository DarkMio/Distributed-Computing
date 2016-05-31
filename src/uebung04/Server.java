package uebung04;

import uebung04.ServerImpl.Connection.ClientConnection;
import uebung04.ServerImpl.Logic.MailImpl;
import uebung04.ServerImpl.Management.ServerThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server{
    private static final int PORT = 5678;
    private static final int CONNECTION_POOL_SIZE = 5;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(CONNECTION_POOL_SIZE);
        ArrayList<ClientConnection> connections = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            // Initiate conversation with client
            System.out.println("Mail Service started and awaiting new clients.");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientConnection conn = new ClientConnection(clientSocket);
                connections = clearConnectionPool(connections);
                if(connections.size() >= CONNECTION_POOL_SIZE) {
                    conn.sendMessage(503, -1, new String[]{"Server too busy"}); // El Mao
                    System.err.println("ERR : New client (" + conn.getUuid() +
                            ") tried to connect, but there are no free sockets.");
                    continue;
                }
                Runnable runnable = new ServerThread(conn);
                executor.execute(runnable);
                connections.add(conn);

                System.out.println("INFO: New Client connected with uuid: " + conn.getUuid());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<ClientConnection> clearConnectionPool(ArrayList<ClientConnection> connections) {
        for(int i = 0; i < connections.size(); i++) {
            ClientConnection c = connections.get(i);
            if(c.state == ClientConnection.ConnectionState.offline) {
                connections.remove(c);
            }
        }
        return connections;
    }
}
