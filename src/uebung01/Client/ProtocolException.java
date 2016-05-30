package uebung01.Client;

/**
 * Created by Mio on 02.05.2016.
 */
public class ProtocolException extends Exception {

    private static final int INVALID_INPUT = -1;
    private static final int INVALID_NUMBER = -2;

    private final int errorCode;

    public ProtocolException(int errorCode) {
        super("ServerImpl exception happened.");
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage(){
        String s = super.getMessage();
        switch(errorCode) {
            case INVALID_INPUT:
                return s + " It encountered invalid input.";
            case INVALID_NUMBER:
                return s + " It encountered an invalid number.";
            default:
                return s + " The exception cause is unknown, error code is: " + errorCode;
        }
    }

}
