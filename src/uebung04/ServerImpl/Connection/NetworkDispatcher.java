package uebung04.ServerImpl.Connection;

import com.google.gson.JsonObject;
import uebung04.ServerImpl.Connection.ClientConnection;
import uebung04.util.JSONSerializer.JSONConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Knows the connection pool and manages command execution and prepares sending of messages.
 */
public class NetworkDispatcher {

    private List<ClientConnection> clients;

    public NetworkDispatcher() {
        clients = new ArrayList<>();
    }

    public void registerClient(ClientConnection client) {
        clients.add(client);
    }

    public void removeClient(ClientConnection client) {
        clients.remove(client);
    }

    /**
     * Sends a message to a specific user
     * @param statusCode the status code of the response
     * @param sequenceNumber the sequence number the server responds to
     * @param user either the self given username or the uuid
     * @param data data segment of a message - can be empty.
     * @return true if sent - false if not found or not being able to send
     */
    public boolean sendTo(int statusCode, int sequenceNumber, String user, String[] data) {
        //noinspection ForLoopReplaceableByForEach - deletion process in possible loop
        for(int i = 0; i < clients.size(); i++) {
            ClientConnection cc = clients.get(i);
            if (cc.getUsername().equalsIgnoreCase(user) || cc.getUuid().equals(user)) {
                JsonObject jso = JSONConverter.serializeServerResponse(statusCode, sequenceNumber, data);
                if (cc.sendMessage(jso.toString())) {
                    return true;
                } else {
                    removeClient(cc);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Sends a given message to all users
     * @param statusCode the status code of the response
     * @param sequenceNumber the sequence number the server responds to
     * @param data data segment of a message - can be empty.
     */
    public void sendToAll(int statusCode, int sequenceNumber, String[] data) {
        JsonObject jso = JSONConverter.serializeServerResponse(statusCode, sequenceNumber, data);
        //noinspection ForLoopReplaceableByForEach - deletion process in possible loop
        for(int i = 0; i < clients.size(); i++) {
            ClientConnection cc = clients.get(i);
            if(!cc.sendMessage(jso.toString())) {
                removeClient(cc);
            }
        }
    }
}
