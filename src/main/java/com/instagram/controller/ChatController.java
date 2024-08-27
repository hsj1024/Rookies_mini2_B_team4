////package com.rookies_talk.controller;
////
////import com.rookies_talk.dto.CreateChatRoomRequest;
////import com.rookies_talk.entity.ChatMessage;
////import com.rookies_talk.entity.ChatRoom;
////import com.rookies_talk.entity.User;
////import com.rookies_talk.service.ChatService;
////import com.rookies_talk.service.UserService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////import java.util.Set;
////
////@RestController
////@RequestMapping("/api/chat")
////public class ChatController {
////
////    @Autowired
////    private ChatService chatService;
////
////    @Autowired
////    private UserService userService;
////
////    @PostMapping("/room")
////    public ChatRoom createRoom(@RequestBody CreateChatRoomRequest request) {
////        // CreateChatRoomRequest에서 users 필드는 사용자 ID(Set<Long>)이므로,
////        // UserService 또는 UserRepository를 사용하여 User 엔티티들을 조회한 후, ChatRoom을 생성해야 합니다.
////        Set<User> users = userService.findUsersByIds(request.getUsers());
////        return chatService.createChatRoom(request.getName(), users);
////    }
////
////
////    //    @PostMapping("/send")
//////    public void sendMessage(@RequestParam Long chatRoomId,
//////                            @RequestParam Long senderId,
//////                            @RequestBody String content) {
//////
////////        User sender = userService.findUserById(senderId); // 실제로 데이터베이스에서 사용자 정보를 가져오는 로직
////////        chatService.sendMessage(chatRoomId, sender, content);
//////        User sender = new User();
//////        sender.setId(senderId);
//////        chatService.sendMessage(chatRoomId, sender, content);
//////    }
////    @PostMapping("/send")
////    public void sendMessage(@RequestBody MessageRequest request) {
////        User sender = new User();
////        sender.setId(request.getSenderId());
////        chatService.sendMessage(request.getChatRoomId(), sender, request.getContent());
////    }
////    @GetMapping("/messages/{chatRoomId}")
////    public List<ChatMessage> getMessages(@PathVariable Long chatRoomId) {
////        List<ChatMessage> messages = chatService.getMessages(chatRoomId);
////        messages.forEach(msg -> System.out.println(msg.getContent()));  // 메시지 내용 로그로 출력
////        return messages;
////    }
////
////    @GetMapping("/printMessages/{chatRoomId}")
////    public void printMessages(@PathVariable Long chatRoomId) {
////        chatService.printMessages(chatRoomId);
////    }
////
////    public static class MessageRequest {
////        private Long chatRoomId;
////        private Long senderId;
////        private String content;
////
////        // Getters and Setters
////        public Long getChatRoomId() {
////            return chatRoomId;
////        }
////
////        public void setChatRoomId(Long chatRoomId) {
////            this.chatRoomId = chatRoomId;
////        }
////
////        public Long getSenderId() {
////            return senderId;
////        }
////
////        public void setSenderId(Long senderId) {
////            this.senderId = senderId;
////        }
////
////        public String getContent() {
////            return content;
////        }
////
////        public void setContent(String content) {
////            this.content = content;
////        }
////    }
////}
//package com.instagram.controller;
//
//import com.instagram.dto.CreateChatRoomRequest;
//import com.instagram.entity.ChatMessage;
//import com.instagram.entity.ChatRoom;
//import com.instagram.entity.User;
//import com.instagram.service.ChatService;
//import com.instagram.service.UserService;
//import com.instagram.service.impl.UserServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/chat")
//public class ChatController {
//
//    @Autowired
//    private ChatService chatService;
//
//    @Autowired
//    private UserServiceImpl userServiceImpl;
//
//    @PostMapping("/room")
//    public ChatRoom createRoom(@RequestBody CreateChatRoomRequest request) {
//        // Set<Long>을 Set<String>으로 변환
//        Set<String> userIds = request.getUsers().stream()
//                .map(String::valueOf)
//                .collect(Collectors.toSet());
//
//        Set<User> users = userServiceImpl.findUsersByStringIds(userIds);
//
//        return chatService.createChatRoom(request.getName(), users);
//    }
//
////    @PostMapping("/send")
////    public void sendMessage(@RequestBody MessageRequest request) {
////        User sender = userService.findUserById(request.getSenderId());
////        chatService.sendMessage(request.getChatRoomId(), sender, request.getContent());
////    }
//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/messages")
//    public ChatMessage sendMessage(ChatMessage message) {
//        return message; // 받은 메시지를 그대로 반환하여 모든 구독자에게 전송
//    }
//
//    @GetMapping("/messages/{chatRoomId}")
//    public List<ChatMessage> getMessages(@PathVariable Long chatRoomId) {
//        return chatService.getMessages(chatRoomId);
//    }
//
//    @GetMapping("/printMessages/{chatRoomId}")
//    public void printMessages(@PathVariable Long chatRoomId) {
//        chatService.printMessages(chatRoomId);
//    }
//
//
//
//    public static class MessageRequest {
//        private Long chatRoomId;
//        private Long senderId;
//        private String content;
//
//        // Getters and Setters
//        public Long getChatRoomId() {
//            return chatRoomId;
//        }
//
//        public void setChatRoomId(Long chatRoomId) {
//            this.chatRoomId = chatRoomId;
//        }
//
//        public Long getSenderId() {
//            return senderId;
//        }
//
//        public void setSenderId(Long senderId) {
//            this.senderId = senderId;
//        }
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//    }
//}

package com.instagram.controller;

import org.springframework.security.core.Authentication;
import com.instagram.entity.User;

import com.instagram.entity.ChatMessage;
import com.instagram.entity.ChatRoom;
import com.instagram.service.ChatService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 채팅방 목록을 반환하기 위해 RestController로 변경
@RequestMapping("/api/chat")
public class ChatController {

    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService; // ChatService를 통해 채팅방 목록을 가져옴

    public ChatController(KafkaTemplate<String, ChatMessage> kafkaTemplate, SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.kafkaTemplate = kafkaTemplate;
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@RequestBody ChatMessage message) {
        String topic = "chat-room-" + message.getChatRoom().getId();
        kafkaTemplate.send(topic, message);
    }

    @KafkaListener(topicPattern = "chat-room-*", groupId = "chat_group")
    public void receiveMessage(ChatMessage message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        String destination = "/topic/messages/" + extractChatRoomIdFromTopic(topic);
        messagingTemplate.convertAndSend(destination, message);
    }

    private String extractChatRoomIdFromTopic(String topic) {
        return topic.split("-")[2]; // "chat-room-1"에서 "1"을 추출
    }

    // 채팅방 목록을 가져오는 API 추가
    @GetMapping("/rooms")
    public List<ChatRoom> getUserChatRooms(Authentication authentication) {
        String userId = ((User) authentication.getPrincipal()).getUserId();
        return chatService.getChatRoomsForUser(userId);
    }
}
