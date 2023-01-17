import java.util.ArrayList;
import java.util.List;

public class MessagingQueue {

    private Queue<Message> messages;

    public MessagingQueue() {
        messages = new LinkedList<Message>();
    }

    public boolean addMessage(Message message) {
        messages.add(message);
    }

    public Queue<Message> getMessages() {
        return messages;
    }

}