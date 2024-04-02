package com.example.livechat.controller;

import com.example.livechat.entity.ChatMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
public class ChatController {
    /*
    private final ChatService chatService;


    public ChatController(ChatService chatService) {
        this.chatService=chatService;
       }

     */

    @MessageMapping("/chat.addUser")
    @SendTo("topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        Map<String, Object> sessionAttributes = simpMessageHeaderAccessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("username", chatMessage.getSender());
        } else {
            log.error("세션 속성이 null입니다.");
        }
        return chatMessage;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

        return chatMessage;
    }
/*
    @MessageMapping("/message/private")
    @SendToUser("/user/queue/reply")
    public ChatMessage sendPrivateMessage(@Payload ChatMessage chatMessage) {
        chatService.sendChatMessage(chatMessage);
        return chatMessage;
    }

 */
}
