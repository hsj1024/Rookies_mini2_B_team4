package com.instagram.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {
    private Long chatRoomId;
    private Long sender;
    private Long recipient;
    private String content;
    private LocalDateTime timestamp;

    // Getters and setters
}
