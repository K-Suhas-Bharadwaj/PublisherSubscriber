import java.util.ArrayList;
import java.util.List;

public class Service {

    Queue<Message> inbox;

    public Service() {
        messages = new LinkedList<Message>();
    }

    public Queue<Message> getInbox() {
        return inbox;
    }
    
    public void subscribeToTopic(Topic topic) {
        return topic.addSubscriberToTopic(this);
    }

    public boolean unsubscribeToTopic(Topic topic) {
        return topic.removeSubscriberToTopic(this);
    }

    public boolean publishMessage(Message message, MessageRouter messageRouter) {
        messageRouter.stream(message);
    }

    public void ingestMessages(MessageRouter messageRouter) {
        while(!inbox.isEmpty()) {
            Message response = processMessage(inbox.poll());
            publishMessage(response, messageRouter);
        }
    }

    publish Message processMessage(Message message) {
        Topic topic = Topic.DefaultResponse;
        Map<String, Value<String>> responseMessage = new HashMap<String, Value<String>>();
        String key = "Default Response";
        Value<String> responseBody = new Value<String>("Request Processed");
        responseMessage.put(key, responseBody);
        return new Message(topic, responseMessage);
    }

}
