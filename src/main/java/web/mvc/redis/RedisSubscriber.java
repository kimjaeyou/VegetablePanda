package web.mvc.redis;

import org.springframework.stereotype.Component;

@Component
public class RedisSubscriber {

    public void handleMessage(String message) {
        System.out.println("Received message: " + message);
    }
}
