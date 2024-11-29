package web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.mvc.dto.MessageReq;
import web.mvc.redis.RedisPublisher;

//뺏길떄
//선호알림
//시간 알림
@RestController
//@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private final RedisPublisher redisPublisher;

    public NotificationController(SimpMessagingTemplate messagingTemplate,RedisPublisher redisPublisher) {
        this.messagingTemplate = messagingTemplate;
        this.redisPublisher = redisPublisher;
    }

    @PostMapping("/send")
    public String sendBidMessage(@RequestBody MessageReq messageRequest) {

        // Publish message to Redis channel
        redisPublisher.publish("notifications", messageRequest.getMessage());


        System.out.println(messageRequest.getMessage());
        messagingTemplate.convertAndSend("/bid/notifications", messageRequest.getMessage());
        return "ok";
    }

    @PostMapping("/sendBidAll")
    public String sendNormalMessage(@RequestBody MessageReq messageRequest) {

        // Publish message to Redis channel
        redisPublisher.publish("notifications", messageRequest.getMessage());

        System.out.println(messageRequest.getMessage());
        messagingTemplate.convertAndSend("/all/notifications", messageRequest.getMessage());
        return "ok";
    }


    @PostMapping("/sendTop")
    public String sendTopMessage(@RequestBody MessageReq messageRequest) {

        // Publish message to Redis channel
        redisPublisher.publish("notifications", messageRequest.getMessage());

        System.out.println(messageRequest.getMessage());
        messagingTemplate.convertAndSend("/top/notifications", messageRequest.getMessage());
        return "ok";
    }

    // 특정 사용자에게 알림 전송
    @PostMapping("/sendToUser")
    public String sendToUser(@RequestBody MessageReq messageRequest) {
        String userId = messageRequest.getUserId(); // 메시지를 받을 사용자 ID
        String message = messageRequest.getMessage();

        // Redis를 통해 메시지 발행
        redisPublisher.publish("notifications", message);

        // 특정 사용자에게 메시지 전송
        messagingTemplate.convertAndSend("/user/"+userId+"/notifications", messageRequest.getMessage());
        return "Message sent to user: " + message;
    }

}
