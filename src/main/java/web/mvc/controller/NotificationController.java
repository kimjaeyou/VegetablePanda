package web.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.MessageReq;
import web.mvc.service.NotificationService;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // /bid topic으로 메시지 전송
    @PostMapping("/send")
    public String sendBidMessage(@RequestBody MessageReq messageRequest) {
        notificationService.sendMessageToTopic("/bid/notifications", messageRequest.getMessage());
        return "Message sent to /bid/notifications: " + messageRequest.getMessage();
    }

    // /all topic으로 메시지 전송
    @PostMapping("/sendBidAll")
    public String sendAllMessage(@RequestBody MessageReq messageRequest) {
        notificationService.sendMessageToTopic("/all/notifications", messageRequest.getMessage());
        return "Message sent to /all/notifications: " + messageRequest.getMessage();
    }

    // /top topic으로 메시지 전송
    @PostMapping("/sendTop")
    public String sendTopMessage(@RequestBody MessageReq messageRequest) {
        notificationService.sendMessageToTopic("/top/notifications", messageRequest.getMessage());
        return "Message sent to /top/notifications: " + messageRequest.getMessage();
    }

    // 특정 사용자에게 메시지 전송
    @PostMapping("/sendToUser")
    public String sendToUser(@RequestBody MessageReq messageRequest) {
        notificationService.sendMessageToUser(messageRequest.getUserId(), messageRequest.getMessage());
        return "Message sent to user: " + messageRequest.getUserId();
    }
}
