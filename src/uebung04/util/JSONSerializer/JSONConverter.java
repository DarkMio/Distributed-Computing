package uebung04.util.JSONSerializer;


import com.google.gson.*;
import uebung04.Client;


/**
 * It might be useful for high scalability to write this as singleton.
 */
public class JSONConverter {

    public final static String COMMAND = "cmd";
    public final static String SEQUENCE_NUMBER = "seq";
    public final static String STATUS_CODE = "status";
    public final static String DATA = "data";
    public final static String PARAMETERS = "params";
    // this way we can stay static and don't have to generate tons of new parser objects.
    private final static JsonParser JSON_PARSER = new JsonParser();

    public static void main(String[] args) {
        JsonObject serverResponse = serializeServerResponse(502, 22, new String[]{"a", "b", "c"});
        JsonObject clientRequest = serializeClientRequest(12, "someCommand", new String[]{"a", "b", "c"});

        ServerMessage sMsg = deserializeServerResponse(serverResponse);
        ClientMessage cMsg = deserializeClientRequest(clientRequest);

        System.err.println(sMsg);
        System.err.println(cMsg);
    }

    /**
     * Converts a request into a JSON exchange format
     * @param code the status code of the message
     * @param sequenceNumber the sequence number of the sent element
     * @param data parameters for this function - as return value
     */
    public static JsonObject serializeServerResponse(final int code, final int sequenceNumber, final String[] data) {
        return getJsonObject(STATUS_CODE, new JsonPrimitive(code), SEQUENCE_NUMBER, new JsonPrimitive(sequenceNumber), DATA, toJsonArray(data));
    }

    public static JsonObject serializeClientRequest(final int sequenceNumber, final String command, final String[] params) {
        return getJsonObject(SEQUENCE_NUMBER, new JsonPrimitive(sequenceNumber), COMMAND, new JsonPrimitive(command), PARAMETERS, toJsonArray(params));
    }

    private static JsonObject getJsonObject(String sequenceNumber2, JsonPrimitive value, String command2, JsonPrimitive value2, String parameters, JsonArray value3) {
        JsonObject jso = new JsonObject();
        jso.add(sequenceNumber2, value);
        jso.add(command2, value2);
        jso.add(parameters, value3);
        return jso;
    }

    private static JsonArray toJsonArray(String[] data) {
        JsonArray jArray = new JsonArray();
        for(String s: data) {
            jArray.add(new JsonPrimitive(s));
        }
        return jArray;
    }

    public static ClientMessage deserializeClientRequest(JsonObject request) {
        JsonPrimitive sequence = (JsonPrimitive) request.get(SEQUENCE_NUMBER);
        JsonPrimitive command = (JsonPrimitive) request.get(COMMAND);
        JsonArray params = (JsonArray) request.get(PARAMETERS);
        if(params == null || command == null || sequence == null) {
            throw new InvalidFormatException("Client Request malformed");
        }
        return new ClientMessage(sequence.getAsInt(), command.getAsString(), getStrings(params));
    }

    public static ClientMessage deserializeClientRequest(String message) {
        return deserializeClientRequest(JSON_PARSER.parse(message).getAsJsonObject());
    }

    public static ServerMessage deserializeServerResponse(JsonObject response) {
        JsonPrimitive status = (JsonPrimitive) response.get(STATUS_CODE);
        JsonPrimitive sequenceNumber = (JsonPrimitive) response.get(SEQUENCE_NUMBER);
        JsonArray data = (JsonArray) response.get(DATA);
        if(status == null || sequenceNumber == null || data == null) {
            throw new InvalidFormatException("Server Request malformed");
        }
        return new ServerMessage(status.getAsInt(), sequenceNumber.getAsInt(), getStrings(data));
    }

    public static ServerMessage deserializeServerResponse(String message) {
        return deserializeServerResponse(JSON_PARSER.parse(message).getAsJsonObject());
    }

    private static String[] getStrings(JsonArray params) {
        final String[] paramArray = new String[params.size()];
        for(int i = 0; i < params.size(); i++) {
            paramArray[i] = params.get(i).getAsString();
        }
        return paramArray;
    }
}
