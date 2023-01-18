import java.util.ArrayList;
import java.util.List;

public class Service implements Runnable {

    private Queue<Message> inbox;

    public Service() {
        messages = new LinkedList<Message>();
        MessageRouter.getInstance().registerService(this);
    }

    public Queue<Message> getInbox() {
        return inbox;
    }
    
    public boolean subscribeToTopic(Topic topic) {
        return topic.addSubscriberToTopic(this);
    }

    public boolean unsubscribeToTopic(Topic topic) {
        return topic.removeSubscriberToTopic(this);
    }

    public void publishMessage(Message message) {
        MessageRouter.getInstance().stream(message);
    }

    public void ingestMessages() {
        while(!inbox.isEmpty()) {
            Message response = processMessage(inbox.poll());
            publishMessage(response);
        }
    }

    publish Message processMessage(Message message) {
        Topic topic = Topic.DEFAULT_RESPONSE;
        Map<String, Value<String>> responseMessage = new HashMap<String, Value<String>>();
        String key = "Default Response";
        Value<String> responseBody = new Value<String>("Request Processed");
        responseMessage.put(key, responseBody);
        return new Message(topic, responseMessage);
    }

    public void run() {
        while(true) {
            ingestMessages();
        }
    }

}
