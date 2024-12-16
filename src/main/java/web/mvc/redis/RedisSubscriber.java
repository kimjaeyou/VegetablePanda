package web.mvc.redis;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisSubscriber {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public RedisSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String channel, String message) {
        messagingTemplate.convertAndSend(channel, message);
    }
}
