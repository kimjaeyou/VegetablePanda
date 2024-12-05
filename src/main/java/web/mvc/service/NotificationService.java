package web.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import web.mvc.redis.RedisPublisher;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisPublisher redisPublisher;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate, RedisPublisher redisPublisher) {
        this.messagingTemplate = messagingTemplate;
        this.redisPublisher = redisPublisher;
    }

    // Redis를 통해 메시지 발행 및 특정 topic으로 전송
    public void sendMessageToTopic(String topic, String message) {
        redisPublisher.publish("notifications", message); // Redis 발행
        messagingTemplate.convertAndSend(topic, message); // 특정 topic으로 전송
    }

    // 특정 사용자에게 메시지 전송
    public void sendMessageToUser(String userId, String message) {
        redisPublisher.publish("notifications", message); // Redis 발행
        messagingTemplate.convertAndSend("/user/" + userId + "/notifications", message); // 특정 사용자로 전송
    }

    // 방송방 유저 메세지 전송
    public void sendMessageTobidUser(String userId, String message) {
        redisPublisher.publish("notifications", message); // Redis 발행
        messagingTemplate.convertAndSend("/biduser/" + userId + "/notifications", message); // 특정 사용자로 전송
    }
}
