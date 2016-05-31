package uebung04.ServerImpl.Logic;

import uebung04.MailInterface;
import uebung04.Server;
import uebung04.ServerImpl.Connection.ClientConnection;
import uebung04.ServerImpl.Management.ServerThread;

import java.util.ArrayList;
import java.util.List;

public class MailImpl implements MailInterface {

    private final static long MAX_TTL = 1000L * 60L * 10L; // 1000ms -> 1sec * 60 -> 1min * 10 -> 10min
    private List<Note> notes;
    private List<ClientConnection> clients;
    private static MailImpl instance;
    private MailImpl() {
        clients = new ArrayList<>();
        notes = new ArrayList<>();
    }

    /**
     * This singleton allows us to share all complex logic between all thread
     * and make sure that it runs in a synchronized state for all transactional methods
     * @return the actual MailImpl instance
     */
    public static synchronized MailImpl getInstance() {
        if(MailImpl.instance == null) {
            MailImpl.instance = new MailImpl();
        }
        return MailImpl.instance;
    }

    @Override
    public boolean login(ClientConnection user) {
        for(ClientConnection s: clients) {
            if(s.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        clients.add(user);
        return true;
    }

    @Override
    public synchronized boolean logout(ClientConnection user) {
        clients.remove(user);
        return true;
    }

    @Override
    public List<ClientConnection> who() {
        return clients;
    }

    @Override
    public long time() {
        return System.currentTimeMillis();
    }

    @Override
    public List<String> ls(String path) {
        return new ArrayList<String>(){{
            add("Not");
            add("Sure");
            add("What");
            add("This");
            add("Is");
            add("'sposed");
            add("to");
            add("do");
        }};
    }

    @Override
    public boolean chat(String username, String message) {
        boolean exists = false;
        for(ClientConnection s: clients) {
            exists = s.getUsername().equals(username);
            if(exists) {
                break;
            }
        }
        return exists;
    }

    @Override
    public boolean notify(String text) {
        for(ClientConnection client : clients) {
            client.sendMessage(200, -1, new String[]{text});
        }
        return true;
    }

    @Override
    public boolean note(String text) {
        notes.add(new Note(text, System.currentTimeMillis()));
        return true;
    }

    @Override
    public List<String> notes() {
        deleteOldNotes();
        ArrayList<String> noteMessages = new ArrayList<>(notes.size());
        for (Note n: notes) {
            noteMessages.add(n.message);
        }
        return noteMessages;
    }

    /**
     * Searches downwards for all messages with timestamp being older than MAX_TTL
     * Fastest implementation I could think of (although this might be overkill)
     */
    private void deleteOldNotes() {
        int i = notes.size() - 1;
        boolean cut = false;
        while(i >= 0) { // go from newest to oldest message
            final Note element = notes.get(i);
            if(element.timestamp < System.currentTimeMillis() - MAX_TTL) {
                cut = true;
                break;
            }
            i--;
        }
        if(cut) { // slice array and replace
            notes = notes.subList(i+1, notes.size());
        }
    }

    public boolean isLoggedIn(ClientConnection client) {

        return clients.contains(client);
    }
}
