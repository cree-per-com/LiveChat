package com.example.livechat.Controller;

import com.example.livechat.Entity.ChatMessage;
import com.example.livechat.Service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {this.chatService=chatService;}
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/message/private")
    @SendToUser("/user/queue/reply")
    public ChatMessage sendPrivateMessage(@Payload ChatMessage chatMessage) {
        chatService.sendChatMessage(chatMessage);
        return chatMessage;
    }
}
