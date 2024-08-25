package com.instagram.dto.mapper;

import com.instagram.dto.FollowDto;
import com.instagram.entity.Follow;

import com.instagram.entity.User;
import com.instagram.exception.ResourceNotFoundException;
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowMapper {

    private final UserRepository userRepository;

    public static FollowDto mapToFollowDto(Follow follow){
        return new FollowDto(
                follow.getId(),
                follow.getFollowingId().getId(),
                follow.getFollowingId().getUserId(),
                follow.getFollowingId().getUserName(),
                follow.getFollowerId().getId(),
                follow.getFollowerId().getUserId(),
                follow.getFollowerId().getUserName()
        );
    }

    public Follow mapToFollow(FollowDto followDto){
        User following = userRepository.findById(followDto.getFollowingId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
        User follower = userRepository.findById(followDto.getFollowerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        return new Follow(
                followDto.getId(),
                following,
                follower
        );
    }
}