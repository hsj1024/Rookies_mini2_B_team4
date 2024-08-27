package com.instagram.repository;

import com.instagram.entity.ChatMessage;
import com.instagram.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c JOIN c.users u WHERE u.userId = :userId")
    List<ChatRoom> findByUserId(String userId);
}


