import java.util.ArrayList;
import java.util.List;

public class Service {

    private String serviceName;
    private List<Topic> subscribingTopics;

    public Service(String serviceName) {
        this.serviceName = serviceName;
        this.subscribingTopics = new ArrayList<Topic>();
    }

    public Service(String serviceName, ArrayList<Topic> subscribingTopics) {
        this.serviceName = serviceName;
        this.subscribingTopics = subscribingTopics;
    }

    public boolean addSubscribingTopic(Topic topic) {
        return subscribingTopics.add(topic);
    }

    public boolean removeSubscribingTopic(Topic topic) {
        return subscribingTopics.remove(topic);
    }

    public boolean publishTopic(Message message, MessagingQueue messagingQueue) {
        messagingQueue.addMessage(message);
    }

    public void ingestMessages(MessagingQueue messagingQueue) {
        for(Message message : messagingQueue.getMessages()) {
            if(subscribingTopics.contains(message.getTopic())) {
                Message response = processMessage(message);
                publishTopic(response, messagingQueue);
            }
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
