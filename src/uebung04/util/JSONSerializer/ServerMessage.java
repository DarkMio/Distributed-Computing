package uebung04.util.JSONSerializer;

public class ServerMessage {
    final int statusCode;
    final int sequenceNumber;
    final String[] data;

    public ServerMessage(int statusCode, int sequenceNumber, String[] data) {
        this.statusCode = statusCode;
        this.sequenceNumber = sequenceNumber;
        this.data = data;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CODE: ").append(statusCode);
        sb.append(" | ").append("SEQ: ").append(sequenceNumber);
        sb.append(" | ").append("DATA: [");
        for(String s: data) {
            sb.append(s).append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
