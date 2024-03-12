package com.example.livechat.Service;

import com.example.livechat.Entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
//1:1 채팅에서 상대방에게 메세지 전송 : /user/youngchae/private
    public void sendChatMessage(ChatMessage chatMessage) {
        messagingTemplate.convertAndSendToUser(chatMessage.getReceiverName(),"/private", chatMessage);
    }
}
