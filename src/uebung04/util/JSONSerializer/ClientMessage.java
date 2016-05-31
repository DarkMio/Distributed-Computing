package uebung04.util.JSONSerializer;

/**
 * Created by Mio on 30.05.2016.
 */
public class ClientMessage {

    public final int sequenceNumber;
    public final String command;
    public final String[] params;

    public ClientMessage(int sequenceNumber, String command, String[] params) {
        this.sequenceNumber = sequenceNumber;
        this.command = command;
        this.params = params;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SEQ: ").append(sequenceNumber);
        sb.append(" | ").append("CMD: ").append(command);
        sb.append(" | ").append("PARAMS: [");
        for(String s: params) {
            sb.append(s).append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
