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

}
