package web.mvc.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import web.mvc.redis.RedisPublisher;

@Service
public class SendTopService {

    private final RedisPublisher redisPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    public SendTopService(RedisPublisher redisPublisher, SimpMessagingTemplate messagingTemplate) {
        this.redisPublisher = redisPublisher;
        this.messagingTemplate = messagingTemplate;
    }

    public void sendTopMessage(String message) {
        // Publish message to Redis channel
        redisPublisher.publish("notifications", message);

        // 메시지를 특정 topic으로 전송
        messagingTemplate.convertAndSend("/top/notifications", message);

        System.out.println("Message sent to topic: " + message);
    }
}
