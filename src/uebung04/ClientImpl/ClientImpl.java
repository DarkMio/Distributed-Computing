package uebung04.ClientImpl;

import uebung04.util.JSONSerializer.ClientMessage;
import uebung04.util.JSONSerializer.ServerMessage;

import java.util.Date;
import java.util.List;

public class ClientImpl {

    private final List<ServerMessage> messages;
    private final List<ClientMessage> sentMessages;

    public ClientImpl(List<ServerMessage> messages, List<ClientMessage> sentMessages) {
        this.messages = messages;
        this.sentMessages = sentMessages;
    }

    public void newResponse() {
        for(int i = 0; i < messages.size(); i++) {
            ServerMessage smsg = messages.get(i);
            for (int j = 0; j < sentMessages.size(); j++) {
                ClientMessage c = sentMessages.get(j);
                if (smsg.sequenceNumber == c.sequenceNumber) {
                    handleResponse(c, smsg);
                    sentMessages.remove(c);
                    messages.remove(smsg);
                    return;
                }
            }
            System.out.println(dataConcat(smsg.data, " >> "));
            messages.remove(smsg);
        }
    }

    private void handleResponse(ClientMessage original, ServerMessage response) {
        switch(original.command.toLowerCase()) {
            case "login":
                System.out.println(response.data[0]);
                break;
            case "time":
                System.out.println("Server Time is: " + new Date(Long.parseLong(response.data[0])).toString());
                break;
            case "logout":
                System.out.println("Logged out.");
                break;
            case "who":
                String s;
                if(response.data.length > 0) {
                    s = "Users: " + dataConcat(response.data, ", ");
                } else {
                    s = "No logged in users.";
                }
                System.out.println(s);
                break;
            case "ls":
                System.out.println("Filepath: " + dataConcat(response.data, " | "));
                break;
            case "chat":
                System.out.println(original.params[0] + "> " + original.params[1]);
                break;
            case "notify":
                break;
            case "note":
                System.out.println("Note added: " + original.params[0]);
                break;
            case "notes":
                System.out.println("All notes: \n" + dataConcat(response.data, "\n"));
                break;
            default:
                System.out.println(dataConcat(original.params, " >> "));
        }
    }

    private static String dataConcat(String[] data, String concat) {
        String s = "";
        for (String str : data) {
            s += str + concat;
        }
        return s.substring(0, s.length() - concat.length());
    }
}
