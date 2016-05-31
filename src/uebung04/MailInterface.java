package uebung04;

import uebung04.ServerImpl.Connection.ClientConnection;
import uebung04.ServerImpl.Management.ServerThread;

import java.util.List;

public interface MailInterface {
    boolean login(ClientConnection user);
    boolean logout(ClientConnection user);
    List<ClientConnection> who();
    long time();
    List<String> ls(String path);
    boolean chat(String username, String message);
    boolean notify(String text);  // might need some identifier
    boolean note(String text);    // ^same applies here
    List<String> notes();
    boolean isLoggedIn(ClientConnection user);
}
