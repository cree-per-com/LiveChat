package com.example.livechat.controller;

import com.example.livechat.entity.ChatMessage;
import com.example.livechat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {this.chatService=chatService;}

    @MessageMapping("/chat.addUser")
    @SendTo("chatroom/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        //웹소켓 세션에 새 유저명을 추가함
        headerAccessor.getSessionAttributes().put("username",chatMessage.getSenderName());
        return chatMessage;
    }

    @MessageMapping("/chat.sendMessage")
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
