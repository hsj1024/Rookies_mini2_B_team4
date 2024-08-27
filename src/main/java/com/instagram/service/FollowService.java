package com.instagram.service;

import com.instagram.dto.FollowDto;
import com.instagram.entity.Follow;
import org.springframework.stereotype.Service;
import com.instagram.entity.User;

import java.util.List;

@Service
public interface FollowService {
    com.instagram.dto.FollowDto followUser(FollowDto followDto);

    void unfollowUser(com.instagram.dto.FollowDto followDto);

    // 특정 사용자가 팔로우한 사용자 목록을 가져오는 메서드
    //List<FollowDto> getFollowing(Long userId);  // 추가된 메서드



}