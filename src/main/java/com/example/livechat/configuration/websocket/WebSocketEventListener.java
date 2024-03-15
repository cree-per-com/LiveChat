package com.example.livechat.configuration.websocket;

import com.example.livechat.entity.ChatMessage;
import com.example.livechat.entity.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageTemplate;
    @EventListener
    public void handleWebSockerDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String)headerAccessor.getSessionAttributes().get("username");
        if (username!=null) {
            log.info("{} 님과의 연결이 끊어졌습니다. ", username);
            var chatMessage = ChatMessage.builder().type(MessageType.LEAVE).build();
            messageTemplate.convertAndSend("/chatroom/public",chatMessage);
        }

    }
}
