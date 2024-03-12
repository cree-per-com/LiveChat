package com.example.livechat.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private MessageType type;
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
}
