package com.instagram.repository;

import com.instagram.entity.ChatMessage;
import com.instagram.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {}


