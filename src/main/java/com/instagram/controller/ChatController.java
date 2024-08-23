//package com.rookies_talk.controller;
//
//import com.rookies_talk.dto.CreateChatRoomRequest;
//import com.rookies_talk.entity.ChatMessage;
//import com.rookies_talk.entity.ChatRoom;
//import com.rookies_talk.entity.User;
//import com.rookies_talk.service.ChatService;
//import com.rookies_talk.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Set;
//
//@RestController
//@RequestMapping("/api/chat")
//public class ChatController {
//
//    @Autowired
//    private ChatService chatService;
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/room")
//    public ChatRoom createRoom(@RequestBody CreateChatRoomRequest request) {
//        // CreateChatRoomRequest에서 users 필드는 사용자 ID(Set<Long>)이므로,
//        // UserService 또는 UserRepository를 사용하여 User 엔티티들을 조회한 후, ChatRoom을 생성해야 합니다.
//        Set<User> users = userService.findUsersByIds(request.getUsers());
//        return chatService.createChatRoom(request.getName(), users);
//    }
//
//
//    //    @PostMapping("/send")
////    public void sendMessage(@RequestParam Long chatRoomId,
////                            @RequestParam Long senderId,
////                            @RequestBody String content) {
////
//////        User sender = userService.findUserById(senderId); // 실제로 데이터베이스에서 사용자 정보를 가져오는 로직
//////        chatService.sendMessage(chatRoomId, sender, content);
////        User sender = new User();
////        sender.setId(senderId);
////        chatService.sendMessage(chatRoomId, sender, content);
////    }
//    @PostMapping("/send")
//    public void sendMessage(@RequestBody MessageRequest request) {
//        User sender = new User();
//        sender.setId(request.getSenderId());
//        chatService.sendMessage(request.getChatRoomId(), sender, request.getContent());
//    }
//    @GetMapping("/messages/{chatRoomId}")
//    public List<ChatMessage> getMessages(@PathVariable Long chatRoomId) {
//        List<ChatMessage> messages = chatService.getMessages(chatRoomId);
//        messages.forEach(msg -> System.out.println(msg.getContent()));  // 메시지 내용 로그로 출력
//        return messages;
//    }
//
//    @GetMapping("/printMessages/{chatRoomId}")
//    public void printMessages(@PathVariable Long chatRoomId) {
//        chatService.printMessages(chatRoomId);
//    }
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

import com.instagram.dto.CreateChatRoomRequest;
import com.instagram.entity.ChatMessage;
import com.instagram.entity.ChatRoom;
import com.instagram.entity.User;
import com.instagram.service.ChatService;
import com.instagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @PostMapping("/room")
    public ChatRoom createRoom(@RequestBody CreateChatRoomRequest request) {
        Set<User> users = userService.findUsersByIds(request.getUsers());
        return chatService.createChatRoom(request.getName(), users);
    }

//    @PostMapping("/send")
//    public void sendMessage(@RequestBody MessageRequest request) {
//        User sender = userService.findUserById(request.getSenderId());
//        chatService.sendMessage(request.getChatRoomId(), sender, request.getContent());
//    }
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message; // 받은 메시지를 그대로 반환하여 모든 구독자에게 전송
    }

    @GetMapping("/messages/{chatRoomId}")
    public List<ChatMessage> getMessages(@PathVariable Long chatRoomId) {
        return chatService.getMessages(chatRoomId);
    }

    @GetMapping("/printMessages/{chatRoomId}")
    public void printMessages(@PathVariable Long chatRoomId) {
        chatService.printMessages(chatRoomId);
    }



    public static class MessageRequest {
        private Long chatRoomId;
        private Long senderId;
        private String content;

        // Getters and Setters
        public Long getChatRoomId() {
            return chatRoomId;
        }

        public void setChatRoomId(Long chatRoomId) {
            this.chatRoomId = chatRoomId;
        }

        public Long getSenderId() {
            return senderId;
        }

        public void setSenderId(Long senderId) {
            this.senderId = senderId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
