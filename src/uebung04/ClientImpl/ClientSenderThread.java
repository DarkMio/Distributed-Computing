package uebung04.ClientImpl;

import uebung04.util.JSONSerializer.ClientMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ClientSenderThread implements Runnable {

    private final ServerConnection connection;
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private int sequenceNumber;

    public ClientSenderThread(ServerConnection connection) {
        this.connection = connection;
        sequenceNumber = 0;
    }

    @Override
    public void run() {
        try {
            while (true) {
                final String in = input.readLine();
                final String[] params = in.split(" ");
                if (params.length < 1) {
                    continue;
                } else if (params.length == 1) {
                    sendCommand(params[0], "");
                } else {
                    sendCommand(params[0], in.substring(params[0].length() + 1));
                }
                if (params[0].toLowerCase().equals("logout")) { // sub thread will die and this client will do so too
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCommand(String command, String params) {
        String strpCommand = command.substring(1).toLowerCase();
        // this is bad, and you should feel bad - also collapsed switch cases are not existent in java
        switch(strpCommand) {
            case "login":
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{params});
                break;
            case "logout":
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{});
                break;
            case "who":
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{});
                break;
            case "time":
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{});
                break;
            case "ls":
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{params});
                break;
            case "chat":
                String[] yawn = params.split(" ");
                if(yawn.length < 1) {
                    break;
                }
                String user = yawn[0];
                String msg = params.substring(user.length() + 1);
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{user, msg});
                break;
            case "notify":
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{params});
                break;
            case "note":
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{params});
                break;
            case "notes":
                connection.sendMessage(sequenceNumber, strpCommand, new String[]{});
                break;
            default:
                connection.sendMessage(sequenceNumber, "notify", new String[]{command + " " + params});
        }
        sequenceNumber++;
    }
}
