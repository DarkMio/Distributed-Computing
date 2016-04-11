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

    public static int fibonacci(final int input) {
        if(input <= 0) {
            return 0;
        } else if(input == 1) {
            return 1;
        } else {
            return fibonacci(input - 2) + fibonacci(input - 1);
        }
    }

    public String processInput(String val) { // Extend for stronger protocols?
        try {
            int value = Integer.parseInt(val);
            return "Fibonacci of " + value + " is: " + fibonacci(value);
        } catch(NumberFormatException e) {
            return "Not a number or invalid number format, disposing...";
        }
    }
}












