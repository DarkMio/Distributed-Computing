package uebung01;

/**
 * Created by Mio on 11.04.2016.
 */
public class FibonacciProtocol {


    public static int processFibonacci(final String input) {
        try{
            int value = Integer.parseInt(input);
            return value;
        } catch(NumberFormatException e) {
            return -2;
        }
    }

    private static void help() {
        System.out.println("Welcome to Fibonacci As A Service. " +
                "It seems like you got confused, so here is a help file for you: " +
                "hilfe : prints this help | " +
                "berechne <number> : calculates the fibonacci number | " +
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
                case "berechne":
                    String number = input.length > 1 ? input[1] : "";
                    int returnVal = processFibonacci(number);
                    return returnVal;
                case "ende":
                    return -5;
                default:
                    return -1;
            }
        }
        return 0;
    }
}












