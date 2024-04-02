package com.example.livechat.configuration.websocket;

import com.example.livechat.configuration.security.JWTFilter;
import com.example.livechat.configuration.security.JWTUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JWTUtil jwtUtil;
    public WebSocketConfig(JWTUtil jwtUtil) {
        this.jwtUtil=jwtUtil;
    }

    @Override  //클라이언트가 웹소켓 서버에 연결할때 사용될 엔드포인트 등록
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setInterceptors(new AuthHandshakeInterceptor(jwtUtil));
    }

    @Override  //메세지 브로커의 동작을 구성
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}
