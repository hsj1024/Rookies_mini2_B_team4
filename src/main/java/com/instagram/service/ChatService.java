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
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import com.instagram.exception.ResourceNotFoundException;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    //    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;






//    @Transactional
//    public ChatRoom createChatRoom(String name, Set<User> users) {
//        // 사용자들이 존재하는지 확인
//        for (User user : users) {
//            if (!userRepository.existsById(user.getId())) {
//                throw new RuntimeException("User with ID " + user.getId() + " does not exist");
//            }
//        }
//
//        // 사용자 수가 2명인지 확인
//        if (users.size() != 2) {
//            throw new IllegalArgumentException("Exactly 2 users are required to create a chat room.");
//        }
//
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setRoomName(name);
//        chatRoom.setUsers(users);
//        return chatRoomRepository.save(chatRoom);
//
//    }

    public ChatRoom createChatRoom(String roomName, Set<User> users) {

        // 사용자 ID 집합으로 변환
        Set<Long> userIds = users.stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        // 동일한 사용자들이 이미 존재하는 채팅방을 찾습니다.
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByUserIds(userIds, userIds.size());
        if (existingRoom.isPresent()) {
            return existingRoom.get(); // 기존 채팅방 반환
        }
        // 기존 채팅방이 없으면 새로운 채팅방 생성
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName);
        chatRoom.setUsers(users);
        return chatRoomRepository.save(chatRoom);
    }
    public ChatRoom createChatRoomForUsers(Set<User> users) {
        String roomName = users.stream()
                .map(User::getId)
                .map(String::valueOf)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("Unnamed Room");

        return createChatRoom(roomName, users);
    }


//    @Transactional
//    public void sendMessage(Long chatRoomId, User sender, String content) {
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
//                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found"));
//
//        if (sender == null) {
//            throw new ResourceNotFoundException("Sender not found");
//        }
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





    public List<ChatMessage> getMessages(Long chatRoomId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return chatMessageRepository.findByChatRoomId(chatRoomId, pageable).getContent();
    }





    public void printMessages(Long chatRoomId) {
        // ChatRoom 존재 여부 확인
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        // ChatRoom에 해당하는 메시지 가져오기
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoomId);

        // 메시지 리스트가 비어있는지 확인
        if (messages.isEmpty()) {
            System.out.println("No messages found in chat room ID: " + chatRoomId);
        } else {
            messages.forEach(msg -> {
                if (msg == null) {
                    System.out.println("Null message encountered!");
                } else {
                    System.out.println("Message: " + msg.getContent() + " from user: " + msg.getSender().getUserName());
                }
            });
        }
    }



    public Page<ChatRoom> getChatRoomsForUser(Long userId, Pageable pageable) {
        return chatRoomRepository.findChatRoomsByUserId(userId, pageable);
    }


}
