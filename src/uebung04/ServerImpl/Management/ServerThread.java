package uebung04.ServerImpl.Management;

import org.omg.CORBA.StringHolder;
import uebung04.MailInterface;
import uebung04.ServerImpl.Connection.ClientConnection;
import uebung04.ServerImpl.Logic.MailImpl;
import uebung04.util.JSONSerializer.ClientMessage;
import uebung04.util.JSONSerializer.JSONConverter;

import java.util.List;

public class ServerThread implements Runnable {

    public ClientConnection client;
    private final MailInterface mail;

    public ServerThread(ClientConnection client) {
        this.client = client;
        mail = MailImpl.getInstance();
    }

    @Override
    public void run() {
        try {
            String message;
            client.sendMessage(200, -1, new String[]{
                    "Message of the Day\n" +
                            "------------------\n" +
                            "We're serving fish today! And not only fish, but chips too!\n" +
                            "Combine them now to our tasty fish'n'chips menu at your next venue.\n" +
                            "Welcome to DecreeNode - supporting fish and ships communities since 2016"
            });
            while ((message = client.receiveMessage()) != null) {
                ClientMessage cmsg = JSONConverter.deserializeClientRequest(message);
                System.out.println("RECV: " + cmsg);
                if(!mail.isLoggedIn(client) && !cmsg.command.toLowerCase().equals("login")) {
                    client.sendMessage(401, cmsg.sequenceNumber, new String[]{"Unauthorized: You may want to use login first"});
                    return;
                }
                switch (cmsg.command.toLowerCase()) {
                    case "login":
                        login(cmsg);
                        break;
                    case "logout":
                        logout(cmsg);
                        return; // this kills the thread
                    case "who":
                        who(cmsg);
                        break;
                    case "time":
                        time(cmsg);
                        break;
                    case "ls":
                        ls(cmsg);
                        break;
                    case "chat":
                        chat(cmsg);
                        break;
                    case "notify":
                        notify(cmsg);
                        break;
                    case "note":
                        note(cmsg);
                        break;
                    case "notes":
                        notes(cmsg);
                        break;
                    default:
                        notRecorgnized(cmsg);
                        break;
                }
            }
        } catch (Exception | Error e) {
            System.err.println("ERR : " + e);
            e.printStackTrace();
        }
        System.out.println("INFO: Bye. Client disconnected with UUID: " + client.getUuid());
    }

    private void login(ClientMessage cmsg) {
        if(cmsg.params.length > 0 && client.getUsername() == null) { // so this shitter has no name and wants a name
            if(mail.login(cmsg, client)) {
                client.setUsername(cmsg.params[0]); // the fuck do I care what he wrote as username(s), he gets the first
                client.sendMessage(200, cmsg.sequenceNumber, new String[]{"Login successful."});
                return;
            }
            client.sendMessage(400, cmsg.sequenceNumber, new String[]{"Login unsuccessful: Username is already in use"});
            return;
        }
        client.sendMessage(400, cmsg.sequenceNumber, new String[]{"Login unsuccessful: No username given or already set"});
    }

    private void logout(ClientMessage cmsg) {
        mail.logout(client);
        client.sendMessage(204, cmsg.sequenceNumber, new String[]{});
        client.closeConnection();
    }

    private void who(ClientMessage cmsg) {
        List<ClientConnection> clients = mail.who();
        String[] arr = new String[clients.size()];
        for (int i = 0; i < clients.size(); i++) {
            ClientConnection c = clients.get(i);
            arr[i] = c.getUsername();
        }
        client.sendMessage(200, cmsg.sequenceNumber, arr);
    }

    private void time(ClientMessage cmsg) {
        client.sendMessage(200, cmsg.sequenceNumber, new String[]{mail.time() + ""});
    }

    private void ls(ClientMessage cmsg) {
        if(cmsg.params.length > 0) {
            List<String> ls = mail.ls(cmsg.params[0]);
            client.sendMessage(200, cmsg.sequenceNumber, ls.toArray(new String[ls.size()]));
            return;
        }
        client.sendMessage(400, cmsg.sequenceNumber, new String[]{"No parameters specified."});
    }

    private void chat(ClientMessage cmsg) {
        if(cmsg.params.length > 1) {
            if(mail.chat(cmsg.params[0], cmsg.params[1])){
                client.sendMessage(204, cmsg.sequenceNumber, new String[]{});
                return;
            }
            client.sendMessage(400, cmsg.sequenceNumber, new String[]{"The given username doesn't seem to exist."});
            return;
        }
        client.sendMessage(400, cmsg.sequenceNumber, new String[]{"Invalid amount of parameters. " +
                "It is " + cmsg.params.length + " and should be >=2"});
    }

    private void notify(ClientMessage cmsg) {
        System.out.println(cmsg);
        if(cmsg.params.length > 0) {
            if(mail.notify(cmsg.params[0])) {
                client.sendMessage(204, cmsg.sequenceNumber, new String[]{});
                return;
            }
            client.sendMessage(503, cmsg.sequenceNumber, new String[]{"Server Networking Error: Network layer is not initialized."});
            return;
        }
        client.sendMessage(400, cmsg.sequenceNumber, new String[]{"No parameters specified"});
    }

    private void note(ClientMessage cmsg) {
        if(cmsg.params.length > 0) {
            mail.note(cmsg.params[0]);
            client.sendMessage(200, cmsg.sequenceNumber, new String[]{});
            return;
        }
        client.sendMessage(400, cmsg.sequenceNumber, new String[]{"No parameters specified"});
    }

    private void notes(ClientMessage cmsg) {
        List<String> notes = mail.notes();
        client.sendMessage(200, cmsg.sequenceNumber, notes.toArray(new String[notes.size()]));
    }

    private void notRecorgnized(ClientMessage cmsg) {
        client.sendMessage(501, cmsg.sequenceNumber, new String[]{"Command " + cmsg.command + " is unknown"});
    }
}
