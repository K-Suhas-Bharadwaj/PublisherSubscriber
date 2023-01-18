import java.util.Set;
import java.util.HashSet;

public class MessageRouter {

    private Set<Service> services;

    public MessagingQueue() {
        services = new HashSet<Service>();
    }

    public boolean registerService(Service service) {
        services.add(service);
    }

    public void stream(Message message) {
        Set<Service> subscribersToMessageTopic = message.getTopic().getSubscribers();
        for(Service service : services) {
            if(subscribersToMessageTopic.contains(service)) {
                service.getInbox().add(message);
            }
        }
    }

}
