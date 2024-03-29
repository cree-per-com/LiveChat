package com.example.livechat.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    private MessageType type;
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
}
