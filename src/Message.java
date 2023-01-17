import java.util.Map;
import java.util.HashMap;

public class Message {

    private Topic topic;
    private Map<String, Value<Generic>> message;

    public Message(Topic topic, HashMap<String, Value<Generic>> message) {
        this.topic = topic;
        this.message = message;
    }

    public Value addValue(String key, Value value) {
        return message.put(key, value);
    }

    public Value removeValue(String key) {
        return message.remove(key);
    }

    public Topic getTopic() {
        return topic;
    }

}
