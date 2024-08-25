package com.instagram.repository;

import com.instagram.entity.Follow;
import com.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
