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

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void listen(ChatMessage message) {
        //messagingTemplate.convertAndSend("/topic/messages/" + message.getChatRoom().getId(), message);
        System.out.println("Received Message: " + message.getContent());
        messagingTemplate.convertAndSend("/topic/messages/" + message.getChatRoom().getId(), message);
    }
}
