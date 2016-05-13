package uebung03;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PinnwandClient {
    public static void main(String args[]) {
        try {
            String name = "pinnwand";
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1337);
            Pinnwand pinnwand = (Pinnwand) registry.lookup(name);
            if(pinnwand.login("null") == 1) {
                for(int i = 0; i < 10; i++) {
                    pinnwand.putMessage(i+": Sample Text");
                    String[] messages = pinnwand.getMessages();
                    for (String s : messages) {
                        System.out.println(s);
                    }
                    System.out.println("----------------");
                    Thread.sleep(1500L);
                }
            } else {
                System.err.println("Password invalid.");
            }
        } catch (Exception e) {
            System.err.println("Pinnwand exception:");
            e.printStackTrace();
        }
    }
}
