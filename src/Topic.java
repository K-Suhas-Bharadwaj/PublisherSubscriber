import java.util.Set;
import java.util.HashSet;

public enum Topic {

    T1,
    T1_RESPONSE,
    T2,
    T2_RESPONSE,
    T3,
    T3_RESPONSE,
    T4,
    T4_RESPONSE,
    T5,
    T5_RESPONSE,
    DEFAULT, //Default Topic
    DEFAULT_RESPONSE;

    private Set<Service> subscribers = new HashSet<Service> ();

    public Set<Service> getSubscribers() {
        return this.subscribers;
    }

    public boolean addSubscriberToTopic(Service service) {
        return this.getSubscribers().add(service);
    }

    public boolean removeSubscriberToTopic(Service service) {
        return this.getSubscribers().remove(service);
    }

}
