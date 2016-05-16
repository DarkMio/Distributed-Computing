package uebung03;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PinnwandServer extends UnicastRemoteObject implements Pinnwand {

    public static void main(String args[]) {
        try {
            Runtime.getRuntime().exec("rmiregistry");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Registry registry;

        Pinnwand pinnwand;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            /*
            if (args.length  > 0)
                registry = LocateRegistry.getRegistry(args[0]);
            else
                registry = LocateRegistry.getRegistry("localhost");
            */

            registry = LocateRegistry.createRegistry(1337);
            Pinnwand pws = new PinnwandServer();
            registry.bind(serviceName, pws);

        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public static String serviceName = "pinnwand";
    final static String PASSWORD = "null";
    List<Message> messages;
    final static long MAX_TTL = 1000L * 10L; // 60L * 10L; // 1000 ms = 1s * 60 = 1m * 10 = 10 minutes
    final static int MAX_NUM_MSGS = 10;

    public PinnwandServer() throws RemoteException {
        messages = new ArrayList<>();
    }

    @Override
    public int login(String password) throws RemoteException {
        if(password.equals(PASSWORD)){
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int getMessageCount() throws RemoteException {
        return messages.size();
    }

    @Override
    public String[] getMessages() throws RemoteException {
        deleteOldMessages();
        String[] output = new String[messages.size()];
        for(int i = 0; i < messages.size(); i++) {
            output[i] = (String) messages.get(i).message;
        }
        return output;
    }

    @Override
    public String getMessage(int index) throws RemoteException {
        if(index + 1 > MAX_NUM_MSGS || index + 1 > messages.size()) {
            return null;
        }
        return (String) messages.get(index).message;
    }

    @Override
    public boolean putMessage(String msg) throws RemoteException {
        if(messages.size() >= MAX_NUM_MSGS) {
            return false;
        }
        messages.add(new Message(msg, System.currentTimeMillis()));
        return true;
    }

    /**
     * Searches downwards for all messages with timestamp being older than MAX_TTL
     * Fastest implementation I could think of (although this might be overkill)
     */
    private void deleteOldMessages() {
        int i = messages.size() - 1;
        boolean cut = false;
        while(i >= 0) { // go from newest to oldest message
            final Message element = messages.get(i);
            if(element.timestamp < System.currentTimeMillis() - MAX_TTL) {
                cut = true;
                break;
            }
            i--;
        }
        if(cut) { // slice array and replace
            messages = messages.subList(i+1, messages.size());
        }
    }

    /**
     * Container Class for messages.
     */
    private class Message{
        public final String message;
        public final long timestamp;

        public Message(String message, long timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }
    }
}
