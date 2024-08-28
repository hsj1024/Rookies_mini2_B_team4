package com.instagram.repository;

import com.instagram.entity.ChatMessage;
import com.instagram.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 특정 사용자(userId)가 속한 모든 채팅방을 조회
    @Query("SELECT DISTINCT cr FROM ChatRoom cr JOIN FETCH cr.users u WHERE u.userId = :userId")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);


}


