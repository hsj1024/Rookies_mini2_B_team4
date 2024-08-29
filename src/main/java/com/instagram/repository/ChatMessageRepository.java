package com.instagram.repository;

import com.instagram.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // ChatRoom ID를 기준으로 모든 메시지를 조회하는 메서드

    // Pageable을 지원하는 메서드 정의
    Page<ChatMessage> findByChatRoomId(Long chatRoomId, Pageable pageable);

    // 특정 채팅방의 메세지 불러오기
    List<ChatMessage> findByChatRoomId(Long chatRoomId);

    //Page<ChatMessage> findByChatIdPage(Long chatRoomId);
}
