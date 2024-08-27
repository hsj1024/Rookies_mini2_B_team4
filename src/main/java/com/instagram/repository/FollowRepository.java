package com.instagram.repository;

import com.instagram.entity.Follow;
import com.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, User> {
    // 팔로워 (해당 유저를 팔로우 하는 유저) 목록
    List<Follow> findByFollowingId(User followingId);
    // 팔로잉 (해당 유저가 팔로우하는 유저) 목록
    List<Follow> findByFollowerId(User followerId);
    // 언팔로우
    void deleteByFollowerIdAndFollowingId(User followerId, User followingId);
    // 팔로워
    Long countByFollowingId(User followingId);
    // 팔로잉 수
    Long countByFollowerId(User followerId);

    Optional<Follow> findByFollowerIdAndFollowingId(User followerId, User followingId);

    // 팔로워 ID로 팔로잉하는 사용자 ID 리스트를 가져옴  보성940추가
    @Query("SELECT f.followingId.userId FROM Follow f WHERE f.followerId.userId = :followerId")
    List<String> findFollowingIdsByFollowerId(@Param("followerId") String followerId);



}
