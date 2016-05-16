package uebung03;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class PinnwandClient {
    public static void main(String args[]) {
        try {
            String name = "pinnwand";
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1337);
            Pinnwand pinnwand = (Pinnwand) registry.lookup(name);
            Scanner in = new Scanner(System.in);
            String input;
            if(pinnwand.login("null") == 1) {
                System.out.println("Pinnwand service. Type 'exit', 'get <num>', 'getall', 'say <text>'.");
                while(!(input = in.nextLine()).equalsIgnoreCase("exit")) {
                    processInput(input, pinnwand);
                }
                System.out.println("Cya!");
            } else {
                System.err.println("Password invalid.");
            }
        } catch (Exception e) {
            System.err.println("Pinnwand exception:");
            e.printStackTrace();
        }
    }

    private static void processInput(String input, Pinnwand p) throws RemoteException {
        String[] commands = input.split(" ");
        switch(commands[0].toLowerCase()) {
            case "get":
                get(commands[1], p);
                break;
            case "getall":
                getAll(p);
                break;
            case "say":
                say(input, p);
                break;
            case "help":
                help();
                break;
            default:
                System.err.println("Command not recorginzed. Try 'help' if stuck.");
        }
    }

    private static void get(String s, Pinnwand p) throws RemoteException {
        try {
            int i = Integer.parseInt(s);
            System.out.println(i + ": " + p.getMessage(i));
        } catch (NumberFormatException e) {
            System.err.println("Your number is in an invalid number (or just might not be a number)");
        }
    }

    private static void getAll(Pinnwand p) throws RemoteException {
        String[] results = p.getMessages();
        System.out.println("Found a total of " + results.length + " message(s).");
        for(int i = 0; i < results.length; i++) {
            System.out.println(i + ": " + results[i]);
        }
    }

    /**
     * Remember: The input will be something like: say [the words he actually wants to say]
     */
    private static void say(String input, Pinnwand p) throws RemoteException {
        String message = input.substring(4);
        if(p.putMessage(message)) {
            System.out.println("Message sent.");
        } else {
            System.out.println("Message was not sent because the board is full.");
        }
    }

    private static void help() {
        System.out.println("Following commands are available:");
        System.out.println("say [message] - adds your message to the board");
        System.out.println("getall        - gets all messages from the board");
        System.out.println("get [number]  - gets a certain message from the board");
        System.out.println("help          - prints this help file");
        System.out.println("exit          - exits the application");
    }
}
