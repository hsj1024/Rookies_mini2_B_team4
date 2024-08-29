package com.instagram.repository;

import com.instagram.entity.ChatMessage;
import com.instagram.entity.ChatRoom;
import org.springframework.data.domain.Page;
import com.instagram.entity.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 특정 사용자(userId)가 속한 모든 채팅방을 조회
//    @Query("SELECT DISTINCT cr FROM ChatRoom cr JOIN FETCH cr.users u WHERE u.userId = :userId")
//    List<ChatRoom> findByUserId(@Param("userId") Long userId);


    // 특정 사용자(userId)가 속한 모든 채팅방을 조회 (페이지네이션 지원)
//    @Query("SELECT DISTINCT cr FROM ChatRoom cr JOIN cr.users u WHERE u.userId = :userId")
//    Page<ChatRoom> findByUserId(@Param("userId") Long userId, Pageable pageable);
    // 특정 사용자(userId)가 속한 모든 채팅방을 조회 (페이지네이션 지원)
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.users u WHERE u.id = :userId")
    Page<ChatRoom> findChatRoomsByUserId(@Param("userId") Long userId, Pageable pageable);

    // 사용자 목록에 기반하여 채팅방을 찾기
//    @Query("SELECT cr FROM ChatRoom cr JOIN cr.users u WHERE u IN :users GROUP BY cr HAVING COUNT(u) = :userCount")
//    Optional<ChatRoom> findByUsers(@Param("users") Set<User> users, @Param("userCount") int userCount);
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.users u WHERE u.id IN :userIds GROUP BY cr HAVING COUNT(u) = :userCount")
    Optional<ChatRoom> findByUserIds(@Param("userIds") Set<Long> userIds, @Param("userCount") int userCount);


}



