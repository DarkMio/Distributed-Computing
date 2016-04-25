package uebung01;

/**
 * Created by Mio on 11.04.2016.
 */
public class FibonacciProtocol {

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++) {
            System.out.println(i + ": " + fibonacci(i));
        }
    }

    public static String processFibonacci(final String input) {
        try{
            int value = Integer.parseInt(input);
            return "Fibonacci of " + value + " is: " + fibonacci(value);
        } catch(NumberFormatException e) {
            return "Not a number or invalid number format, disposing...";
        }
    }

    public static int fibonacci(final int input) {
        if(input <= 0) {
            return 0;
        } else if(input == 1) {
            return 1;
        } else {
            return fibonacci(input - 2) + fibonacci(input - 1);
        }
    }

    private static String help() {
        return "Welcome to Fibonacci As A Service. " +
                "It seems like you got confused, so here is a help file for you: " +
                "hilfe : prints this help | " +
                "berechne <number> : calculates the fibonacci number | " +
                "ende : closes the connection";
    }

    public String processInput(final String val) { // Extend for stronger protocols?

        final String[] input = val.split(" ");
        if (input.length < 1) {
            return "Invalid data";
        } else {
            switch (input[0].toLowerCase()) {
                case "hilfe":
                    return help();
                case "berechne":
                    String number = input.length > 1 ? input[1] : "";
                    return processFibonacci(number);
                case "ende":
                    return "Exiting...";
                default:
                    return "No valid parameter given, have you tried 'help'?";
            }
        }

    }
}












