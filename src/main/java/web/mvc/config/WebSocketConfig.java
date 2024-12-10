package web.mvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user","/bid","/top","/end","/biduser"); // 클라이언트로 메시지를 전달하는 prefix
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 서버로 메시지를 보낼 때의 prefix
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println(" registerStompEndpoints(StompEndpointRegistry registry...");
        registry.addEndpoint("/ws") // WebSocket 엔드포인트
                .setAllowedOrigins("https://vegetablepanda.p-e.kr",
                        "https://www.vegetablepanda.p-e.kr");
        //.withSockJS(); // SockJS를 통한 WebSocket fallback 지원
    }


}
