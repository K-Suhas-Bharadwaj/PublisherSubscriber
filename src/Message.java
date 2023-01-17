import java.util.Map;
import java.util.HashMap;

public class Message {

    private Map<String, Value> message;

    public Message(HashMap<String, Value> message) {
        this.message = message;
    }

    public Value addValue(String key, Value value) {
        return message.put(key, value);
    }

    public Value removeValue(String key) {
        return message.remove(key);
    }

}
