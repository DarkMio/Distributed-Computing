package uebung04.ClientImpl;

import uebung04.ServerImpl.Connection.ClientConnection;
import uebung04.util.JSONSerializer.JSONConverter;
import uebung04.util.JSONSerializer.ServerMessage;

import java.util.List;

public class ClientReceiverThread implements Runnable {

    private final List<ServerMessage> queue;
    private final ServerConnection connection;
    private final ClientImpl client;

    public ClientReceiverThread(List<ServerMessage> queue, ServerConnection connection, ClientImpl client) {
        this.queue = queue;
        this.connection = connection;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = connection.receiveMessage()) != null) {
                if (connection.state == ServerConnection.ConnectionState.offline) {
                    break;
                }
                queue.add(JSONConverter.deserializeServerResponse(message));
                client.newResponse();
            }
        } catch (Exception e) {
            connection.state = ServerConnection.ConnectionState.offline;
            System.out.println(e.toString());
        }
    }
}
