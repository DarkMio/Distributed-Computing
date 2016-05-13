package uebung03;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ObjID;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

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

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    public static String serviceName = "pinnwand";
    final static String PASSWORD = "null";
    List<List<Object>> messages;
    final static long MAX_TTL = 1000L * 10L; // 10 seconds
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
        String[] output = new String[messages.size()];
        deleteOldMessages();
        for(int i = 0; i < messages.size(); i++) {
            output[i] = (String) messages.get(i).get(0);
        }
        return output;
    }

    @Override
    public String getMessage(int index) throws RemoteException {
        if(index + 1 > MAX_NUM_MSGS || index + 1 > messages.size()) {
            return null;
        }
        return (String) messages.get(index).get(0);
    }

    @Override
    public boolean putMessage(String msg) throws RemoteException {
        if(messages.size() >= MAX_NUM_MSGS) {
            return false;
        }
        messages.add(
                new ArrayList<Object>(){{
                    add(msg);
                    add(System.currentTimeMillis());
                }}
        );
        return true;
    }

    private void deleteOldMessages() {
        for (int i = 0; i < messages.size(); i++) {
            List element = messages.get(i);
            if ((long) element.get(1) < System.currentTimeMillis() - MAX_TTL) {
                messages.remove(i); // cya
            }
        }
    }
}
