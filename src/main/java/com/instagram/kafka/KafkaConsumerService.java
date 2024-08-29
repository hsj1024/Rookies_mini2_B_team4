package com.instagram.kafka;

import com.instagram.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public KafkaConsumerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topicPattern = "chat-room-.*", groupId = "chat-group")
    public void listen(ChatMessage message) {
        String destination = "/topic/messages/" + message.getRecipient().getId();
        messagingTemplate.convertAndSend(destination, message);
    }

}
