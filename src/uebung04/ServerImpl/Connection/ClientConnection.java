package uebung04.ServerImpl.Connection;

import uebung04.util.JSONSerializer.JSONConverter;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

public class ClientConnection {
    public enum ConnectionState{online, offline}
    final private Socket clientSocket;
    final private String uuid;  // we give the client a unique username
    private String username;    // and the client gets a username at some point, too
    final private BufferedReader incoming;
    final private BufferedWriter outgoing;
    public ConnectionState state;

    public ClientConnection(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        state = ConnectionState.online;
        uuid = UUID.randomUUID().toString();
        incoming = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outgoing = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    }

    /**
     * Tries to send message to the client.
     * @param message
     * @return yields true if sent, false if it failed.
     */
    public boolean sendMessage(String message) {
        try {
            outgoing.write(message);
            outgoing.flush();
            System.err.print("SENT: " + message);
            return true;
        } catch (IOException e) {
            state = ConnectionState.offline;
            //e.printStackTrace();
            return false;
        }
    }

    public boolean sendMessage(int statusCode, int sequenceNumber, String[] data) {
        return sendMessage(JSONConverter.serializeServerResponse(statusCode, sequenceNumber, data).toString() + "\n");
    }

    /**
     * Tries to receive a message from a client.
     * @return Returns the message if received - null if it failed: At this point the client might have died
     */
    public String receiveMessage() {
        try {
            return incoming.readLine();
        } catch (IOException e) {
            state = ConnectionState.offline;
            return null;
        }
    }

    public void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("I tried to close the client connection but I couldn't. :(");
            e.printStackTrace();
        }
        state = ConnectionState.offline;
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
