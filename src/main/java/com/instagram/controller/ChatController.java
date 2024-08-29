package com.instagram.controller;

import com.instagram.dto.CreateChatRoomRequest;
import com.instagram.dto.CustomUserDetail;
import com.instagram.entity.ChatMessage;
import com.instagram.entity.ChatRoom;
import com.instagram.entity.User;
import com.instagram.repository.ChatMessageRepository;
import com.instagram.repository.ChatRoomRepository;
import com.instagram.repository.UserRepository;
import com.instagram.security.CustomUserDetailsService;
import com.instagram.service.ChatService;
import com.instagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.instagram.exception.ResourceNotFoundException;

import java.util.*;


import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController // 채팅방 목록을 반환하기 위해 RestController로 변경
@RequestMapping("/api/chat")
public class ChatController {

    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final UserService userService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository; // 필드 주입
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);


    public ChatController(KafkaTemplate<String, ChatMessage> kafkaTemplate, SimpMessagingTemplate messagingTemplate, ChatService chatService, UserService userService) {
        this.kafkaTemplate = kafkaTemplate;
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.userService = userService;
    }


//    @MessageMapping("/chat.sendMessage")
//    public void sendMessage(@RequestBody ChatMessage message) {
//        // 수신자 정보 설정
//        Long recipientId = message.getRecipient().getId();  // 프론트엔드에서 전달된 recipientId를 사용
//        User recipient = userRepository.findById(recipientId)
//                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with ID: " + recipientId));
//        message.setRecipient(recipient);
//
//        String topic = "chat-room-" + message.getChatRoom().getId();
//
//        // 메시지 저장
//        chatMessageRepository.save(message);  // 메시지를 데이터베이스에 저장
//
//        kafkaTemplate.send(topic, message);  // 메시지를 Kafka를 통해 전달
//    }

//    @MessageMapping("/chat.sendMessage")
//    public void sendMessage(@RequestBody ChatMessage message) {
//        // 수신자 정보 설정
//        if (message.getRecipient() == null) {
//            Long recipientId = message.getRecipient().getId();
//            User recipient = userRepository.findById(recipientId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with ID: " + recipientId));
//            message.setRecipient(recipient);
//        }
//
//        String topic = "chat-room-" + message.getChatRoom().getId();
//
//        // 메시지 저장
//        chatMessageRepository.save(message);  // 메시지를 데이터베이스에 저장
//
//        kafkaTemplate.send("chat-topic", message);  // 메시지를 Kafka를 통해 전달
//    }


    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@RequestBody ChatMessage message) {
        logger.info("Received message: {}", message);

        // 로그 추가: 수신된 JSON 데이터 출력
        logger.info("Raw received data:");
        logger.info("chatRoomId: {}", message.getChatRoom() != null ? message.getChatRoom().getId() : "null");
        logger.info("senderId: {}", message.getSender() != null ? message.getSender().getId() : "null");
        logger.info("recipientId: {}", message.getRecipient() != null ? message.getRecipient().getId() : "null");
        logger.info("content: {}", message.getContent());
        logger.info("timestamp: {}", message.getTimestamp());

        // ChatRoom 설정
        if (message.getChatRoom() == null || message.getChatRoom().getId() == null) {
            logger.error("ChatRoom is null. Cannot process the message.");
            throw new IllegalArgumentException("ChatRoom must not be null");
        }
        ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoom().getId())
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found with ID: " + message.getChatRoom().getId()));
        message.setChatRoom(chatRoom);

        // Sender 설정
        if (message.getSender() == null || message.getSender().getId() == null) {
            logger.error("Sender is null. Cannot process the message.");
            throw new IllegalArgumentException("Sender must not be null");
        }
        User sender = userRepository.findById(message.getSender().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found with ID: " + message.getSender().getId()));
        message.setSender(sender);

        // Recipient 설정
        if (message.getRecipient() == null || message.getRecipient().getId() == null) {
            logger.error("Recipient is null. Cannot process the message.");
            throw new IllegalArgumentException("Recipient must not be null");
        }
        User recipient = userRepository.findById(message.getRecipient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found with ID: " + message.getRecipient().getId()));
        message.setRecipient(recipient);

        logger.info("Recipient found: {}", recipient);

        String topic = "chat-room-" + message.getChatRoom().getId();
        logger.info("Sending message to topic: {}", topic);

        // 메시지 저장
        chatMessageRepository.save(message);  // 메시지를 데이터베이스에 저장
        logger.info("Message saved to database: {}", message);

        kafkaTemplate.send(topic, message);  // 메시지를 Kafka를 통해 전달
        logger.info("Message sent to Kafka topic: {}", topic);
    }




    @KafkaListener(topicPattern = "chat-room-*", groupId = "chat_group")
    public void receiveMessage(ChatMessage message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        logger.info("Received message from Kafka topic: {}", topic);

        // 메시지를 데이터베이스에 저장
        chatMessageRepository.save(message);
        logger.info("Message saved to database: {}", message);

        String destination = "/topic/messages/" + extractChatRoomIdFromTopic(topic);
        logger.info("Sending message to WebSocket destination: {}", destination);

        messagingTemplate.convertAndSend(destination, message);
    }

    private String extractChatRoomIdFromTopic(String topic) {
        return topic.split("-")[2]; // "chat-room-1"에서 "1"을 추출
    }

    // 채팅방 목록을 가져오는 API
    @GetMapping("/rooms")
    public List<ChatRoom> getUserChatRooms(Authentication authentication,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        User user = customUserDetail.getUser();
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<ChatRoom> chatRoomPage = chatService.getChatRoomsForUser(userId, pageable);

        List<ChatRoom> chatRooms = chatRoomPage.getContent();

        for (ChatRoom room : chatRooms) {
            // 현재 사용자가 참여하고 있는 채팅방에서 다른 사용자(들)의 이름을 가져옵니다.
            Set<User> users = room.getUsers();
            User otherUser = users.stream()
                    .filter(u -> !u.getId().equals(userId))
                    .findFirst()
                    .orElse(null);

            if (otherUser != null) {
                room.setRoomName(otherUser.getUserName()+ " 와의 채팅 ");
            } else {
                room.setRoomName("Chat Room " + room.getId());
            }
        }

        // 디버깅을 위한 로그 추가
        System.out.println("Fetched Chat Rooms: " + chatRooms);
        return chatRooms;
    }
    @PostMapping("/room")
    public ChatRoom createRoom(@RequestBody CreateChatRoomRequest request) {
        try {
            // 사용자 ID로 User 객체를 가져오는 로직
            Set<User> users = new HashSet<>();
            for (Long userId : request.getUsers()) {
                // id로 User 객체를 조회
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
                users.add(user);
            }

            System.out.println("Users in the chat room:");
            for (User user : users) {
                System.out.println("User ID: " + user.getId() + ", Username: " + user.getUserName());
            }
            return chatService.createChatRoom(request.getName(), users);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating chat room", e);
        }
    }


    // 채팅방의 메시지 목록을 가져오는 API (페이징 지원)
//     @GetMapping("/messages/{chatRoomId}")
//     public List<ChatMessage> getMessages(@PathVariable Long chatRoomId,
//                                          @RequestParam Optional<Integer> page,
//                                          @RequestParam Optional<Integer> size) {
//         int pageNo = page.orElse(0); // 기본값으로 페이지 번호 0
//         int pageSize = size.orElse(10); // 기본값으로 페이지 크기 10
//         return chatService.getMessages(chatRoomId, pageNo, pageSize);
//     }
    @GetMapping("/messages/{chatRoomId}")
    public List<ChatMessage> getMessages(@PathVariable Long chatRoomId,
                                         @RequestParam Optional<Integer> page,
                                         @RequestParam Optional<Integer> size) {
        int pageNo = page.orElse(0); // 기본값으로 페이지 번호 0
        int pageSize = size.orElse(10); // 기본값으로 페이지 크기 10
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // 특정 채팅방의 메시지 조회 (페이징 지원)
        Page<ChatMessage> messagePage = chatMessageRepository.findByChatRoomId(chatRoomId, pageable);
        return messagePage.getContent(); // 페이지에서 메시지 내용만 추출하여 반환
    }


    //    @GetMapping("/{chatRoomId}/recipient")
//    public ResponseEntity<Map<String, Long>> getRecipientId(@PathVariable Long chatRoomId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        //authentication.getPrincipal()을 CustomUserDetail로 캐스팅한 후 사용자 ID 가져오기
//        CustomUserDetail currentUserDetail = (CustomUserDetail) authentication.getPrincipal();
//        Long currentUserId = currentUserDetail.getUser().getId();
//
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
//                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found for ID " + chatRoomId));
//
//        Long recipientId = chatRoom.getUsers().stream()
//                .filter(users -> !users.getId().equals(currentUserId))
//                .map(User::getId)
//                .findFirst()
//                .orElseThrow(() -> new ResourceNotFoundException("No other user found in chat room " + chatRoomId));
//
//        Map<String, Long> response = new HashMap<>();
//        response.put("recipientId", recipientId);
//        return ResponseEntity.ok(response);
//    }
    @GetMapping("/{chatRoomId}/recipient")
    public ResponseEntity<Map<String, Long>> getRecipientId(@PathVariable Long chatRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // CustomUserDetail로 인증 객체를 캐스팅
        CustomUserDetail currentUserDetail = (CustomUserDetail) authentication.getPrincipal();
        Long currentUserId = currentUserDetail.getUser().getId();

        // 채팅방을 찾고 수신자 ID를 결정
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("ChatRoom not found for ID " + chatRoomId));

        Long recipientId = chatRoom.getUsers().stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .map(User::getId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No other user found in chat room " + chatRoomId));

        // 응답으로 수신자 ID 반환
        Map<String, Long> response = new HashMap<>();
        response.put("recipientId", recipientId);
        return ResponseEntity.ok(response);
    }

}
