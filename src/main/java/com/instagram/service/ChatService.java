//package com.rookies_talk.service;
//
//import com.rookies_talk.entity.ChatMessage;
//import com.rookies_talk.entity.ChatRoom;
//import com.rookies_talk.entity.User;
//import com.rookies_talk.repository.ChatMessageRepository;
//import com.rookies_talk.repository.ChatRoomRepository;
//import com.rookies_talk.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class ChatService {
//
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//
//    @Autowired
//    private ChatMessageRepository chatMessageRepository;
//
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//    @Autowired
//    private UserRepository userRepository;  // UserRepository를 주입받습니다.
//
//    @Transactional
//    public ChatRoom createChatRoom(String name, Set<User> users) {
//        // 사용자들이 존재하는지 확인
//        for (User user : users) {
//            if (!userRepository.existsById(user.getId())) {
//                throw new RuntimeException("User with ID " + user.getId() + " does not exist");
//            }
//        }
//
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setName(name);
//        chatRoom.setUsers(users);
//        return chatRoomRepository.save(chatRoom);
//    }
//
////    @Transactional
////    public void sendMessage(Long chatRoomId, User sender, String content) {
////        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
////                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
////
////        ChatMessage message = new ChatMessage();
////        message.setChatRoom(chatRoom);
////        message.setSender(sender);
////        message.setContent(content);
////        message.setTimestamp(LocalDateTime.now());
////
////        kafkaTemplate.send("chat-topic", message);
////        chatMessageRepository.save(message);
////    }
//    @Transactional
//    public void sendMessage(Long chatRoomId, Long senderId, String content) {
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
//                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
//
//        User sender = userRepository.findById(senderId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        ChatMessage message = new ChatMessage();
//        message.setChatRoom(chatRoom);
//        message.setSender(sender);
//        message.setContent(content);
//        message.setTimestamp(LocalDateTime.now());
//
//        chatMessageRepository.save(message);
//        kafkaTemplate.send("chat-topic", message);
//    }
//
//
//    public List<ChatMessage> getMessages(Long chatRoomId) {
//
//        return chatMessageRepository.findByChatRoomId(chatRoomId);
//    }
//
//    public void printMessages(Long chatRoomId) {
//        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
//        messages.forEach(msg -> System.out.println(msg.getContent()));
//    }
//}

////2
//package com.instagram.service;
//
//import com.instagram.entity.ChatMessage;
//import com.instagram.entity.ChatRoom;
//import com.instagram.entity.User;
//import com.instagram.repository.ChatMessageRepository;
//import com.instagram.repository.ChatRoomRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//
//@Service
//public class ChatService {
//
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//
//    @Autowired
//    private ChatMessageRepository chatMessageRepository;
//
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Transactional
//    public ChatRoom createChatRoom(String name, Set<User> users) {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setName(name);
//        chatRoom.setUsers(users);
//        return chatRoomRepository.save(chatRoom);
//    }
//
//    @Transactional
//    public void sendMessage(Long chatRoomId, User sender, String content) {
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
//                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));
//
//        ChatMessage message = new ChatMessage();
//        message.setChatRoom(chatRoom);
//        message.setSender(sender);
//        message.setContent(content);
//        message.setTimestamp(LocalDateTime.now());
//
//        kafkaTemplate.send("chat-topic", message);
//        chatMessageRepository.save(message);
//    }
//
//    public List<ChatMessage> getMessages(Long chatRoomId) {
//        return chatMessageRepository.findByChatRoomId(chatRoomId);
//    }
//
//    public void printMessages(Long chatRoomId) {
//        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
//        messages.forEach(msg -> System.out.println(msg.getContent()));
//    }
//
//    public List<ChatRoom> getChatRoomsForUser(String userId) {
//        // userId로 채팅방 목록을 가져오는 로직 추가
//        return chatRoomRepository.findByUserId(userId);
//    }
//}
package com.instagram.service;

import com.instagram.entity.ChatMessage;
import com.instagram.entity.ChatRoom;
import com.instagram.entity.User;
import com.instagram.repository.ChatMessageRepository;
import com.instagram.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;

    @Transactional
    public ChatRoom createChatRoom(String name, Set<User> users) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setUsers(users);
        return chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void sendMessage(Long chatRoomId, User sender, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        ChatMessage message = new ChatMessage();
        message.setChatRoom(chatRoom);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        kafkaTemplate.send("chat-topic", message);
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessages(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    public void printMessages(Long chatRoomId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);
        messages.forEach(msg -> System.out.println(msg.getContent()));
    }

    public List<ChatRoom> getChatRoomsForUser(String userId) {
        // userId로 채팅방 목록을 가져오는 로직
        return chatRoomRepository.findByUserId(userId);
    }
}
