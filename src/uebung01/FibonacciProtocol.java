package uebung01;

public class FibonacciProtocol {


    public static int processFibonacci(final String input) {
        try{
            return Integer.parseInt(input);
        } catch(NumberFormatException e) {
            System.out.println("Not a known number format");
            return Integer.MAX_VALUE;
        }
    }

    private static void help() {
        System.out.println("Welcome to Fibonacci As A Service.\n" +
                "It seems like you got confused, so here is a help file for you:\n" +
                "hilfe : prints this help\n" +
                "berechne <number> : calculates the fibonacci number\n" +
                "ende : closes the connection");
    }

    public static int processInput(final String val) { // Extend for stronger protocols?
        final String[] input = val.split(" ");
        if (input.length < 1) {
            return -1;
        } else {
            switch (input[0].toLowerCase()) {
                case "hilfe":
                    help();
                    return Integer.MAX_VALUE;
                case "berechne":
                    String number = input.length > 1 ? input[1] : "";
                    return processFibonacci(number);
                case "ende":
                    return Integer.MIN_VALUE;
                default:
                    System.out.println("Not a command, try the help with: hilfe");
                    return Integer.MAX_VALUE;
            }
        }
    }
}