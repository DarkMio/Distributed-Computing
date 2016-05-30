package uebung04.util.JSONSerializer;


import com.google.gson.*;
import uebung04.Client;

public class JSONConverter {

    final boolean isClient;
    private int sequenceNumber;
    private Gson gson;

    public final static String COMMAND = "cmd";
    public final static String SEQUENCE_NUMBER = "seq";
    public final static String STATUS_CODE = "status";
    public final static String DATA = "data";
    public final static String PARAMETERS = "params";

    public static void main(String[] args) {
        JSONConverter server = new JSONConverter(false);
        JSONConverter client = new JSONConverter(true);

        JsonObject serverResponse = client.serializeServerResponse(502, 22, new String[]{"a", "b", "c"});
        JsonObject clientRequest = server.serializeClientRequest(12, "someCommand", new String[]{"a", "b", "c"});

        ServerMessage sMsg = client.deserializeServerResponse(serverResponse);
        ClientMessage cMsg = client.deserializeClientRequest(clientRequest);

        System.err.println(sMsg);
        System.err.println(cMsg);
    }

    public JSONConverter(boolean isClient)  {
        this.isClient = isClient;
        gson = new Gson();
        if(isClient) {
            sequenceNumber = 0;
        }
    }
    /**
     * Converts a request into a JSON exchange format
     * @param code the status code of the message
     * @param sequenceNumber the sequence number of the sent element
     * @param data parameters for this function - as return value
     */
    public JsonObject serializeServerResponse(final int code, final int sequenceNumber, final String[] data) {
        return getJsonObject(STATUS_CODE, new JsonPrimitive(code), SEQUENCE_NUMBER, new JsonPrimitive(sequenceNumber), DATA, toJsonArray(data));
    }

    public JsonObject serializeClientRequest(final int sequenceNumber, final String command, final String[] params) {
        return getJsonObject(SEQUENCE_NUMBER, new JsonPrimitive(sequenceNumber), COMMAND, new JsonPrimitive(command), PARAMETERS, toJsonArray(params));
    }

    private JsonObject getJsonObject(String sequenceNumber2, JsonPrimitive value, String command2, JsonPrimitive value2, String parameters, JsonArray value3) {
        JsonObject jso = new JsonObject();
        jso.add(sequenceNumber2, value);
        jso.add(command2, value2);
        jso.add(parameters, value3);
        return jso;
    }

    private JsonArray toJsonArray(String[] data) {
        JsonArray jArray = new JsonArray();
        for(String s: data) {
            jArray.add(new JsonPrimitive(s));
        }
        return jArray;
    }

    public ClientMessage deserializeClientRequest(JsonObject request) {
        JsonPrimitive sequence = (JsonPrimitive) request.get(SEQUENCE_NUMBER);
        JsonPrimitive command = (JsonPrimitive) request.get(COMMAND);
        JsonArray params = (JsonArray) request.get(PARAMETERS);
        if(params == null || command == null || sequence == null) {
            throw new InvalidFormatException("Client Request malformed");
        }
        return new ClientMessage(sequence.getAsInt(), command.getAsString(), getStrings(params));
    }

    public ServerMessage deserializeServerResponse(JsonObject response) {
        JsonPrimitive status = (JsonPrimitive) response.get(STATUS_CODE);
        JsonPrimitive sequenceNumber = (JsonPrimitive) response.get(SEQUENCE_NUMBER);
        JsonArray data = (JsonArray) response.get(DATA);
        if(status == null || sequenceNumber == null || data == null) {
            throw new InvalidFormatException("Server Request malformed");
        }
        return new ServerMessage(status.getAsInt(), sequenceNumber.getAsInt(), getStrings(data));
    }

    private String[] getStrings(JsonArray params) {
        final String[] paramArray = new String[params.size()];
        for(int i = 0; i < params.size(); i++) {
            paramArray[i] = params.get(i).getAsString();
        }
        return paramArray;
    }
}
