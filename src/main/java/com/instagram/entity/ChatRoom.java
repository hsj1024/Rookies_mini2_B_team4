package com.instagram.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 방 이름을 저장할 필드, userName 대신 roomName으로 변경
    private String roomName;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "chatroom_user",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id") // User의 id와 매핑
    )
    private Set<User> users = new HashSet<>();
}
