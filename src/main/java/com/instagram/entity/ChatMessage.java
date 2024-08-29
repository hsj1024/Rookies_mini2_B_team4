//package com.instagram.entity;
//
//import lombok.Data;
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//public class ChatMessage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "chatroom_id")
//    private ChatRoom chatRoom;
//
//    @ManyToOne
//    @JoinColumn(name = "sender_id")
//    private User sender;
//
//    private String content;
//    private LocalDateTime timestamp;
//}


package com.instagram.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "chat_message")

public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)  // Foreign key to ChatRoom
    private ChatRoom chatRoom;  // ChatRoom 객체와 매핑되는 필드

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)  // Foreign key to User
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)  // Foreign key to User (receiver)
    private User recipient;  // 새로 추가된 필드


    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();


}
