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
//뺏길떄
//선호알림
//시간 알림
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody MessageReq messageRequest) {
        // 메시지를 특정 topic으로 전송
        System.out.println(messageRequest.getMessage());
        messagingTemplate.convertAndSend("/bid/notifications", messageRequest.getMessage());
        messagingTemplate.convertAndSend("/topic/notifications", messageRequest.getMessage());
        return "ok";
    }
}
