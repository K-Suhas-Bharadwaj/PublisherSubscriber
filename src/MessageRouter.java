import java.util.Set;
import java.util.HashSet;

public class MessageRouter {

    private static MessageRouter messageRouter = null;

    private Set<Service> services;

    private MessageRouter() {
        services = new HashSet<Service>();
    }

    public static MessageRouter getInstance() {
        if(messageRouter == null) {
            messageRouter = new MessageRouter();
        }
        return messageRouter;
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
