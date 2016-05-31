package uebung04.ClientImpl;

import uebung04.util.JSONSerializer.ServerMessage;

import java.util.List;

public class ClientImpl {

    private final List<ServerMessage> messages;

    public ClientImpl(List<ServerMessage> messages) {
        this.messages = messages;
    }

    public void newResponse() {
        for(int i = 0; i < messages.size(); i++) {
            ServerMessage smsg = messages.get(i);
            System.out.println("RECV: " + smsg);
            messages.remove(smsg);
        }
    }
}
