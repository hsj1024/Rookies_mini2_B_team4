package com.instagram.service;

import com.instagram.dto.FollowDto;
import org.springframework.stereotype.Service;

@Service
public interface FollowService {
    com.instagram.dto.FollowDto followUser(FollowDto followDto);

    void unfollowUser(com.instagram.dto.FollowDto followDto);


}