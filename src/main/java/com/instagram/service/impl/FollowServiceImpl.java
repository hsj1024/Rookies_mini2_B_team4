package com.instagram.service.impl;


import com.instagram.dto.FollowDto;
import com.instagram.dto.mapper.FollowMapper;
import com.instagram.entity.Follow;
import com.instagram.entity.User;
import com.instagram.exception.ResourceNotFoundException;
import com.instagram.repository.FollowRepository;
import com.instagram.repository.UserRepository;
import com.instagram.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FollowMapper followMapper;

    @Override
    public FollowDto followUser(FollowDto followDto) {
        Follow follow = followMapper.mapToFollow(followDto);

        if (follow.getFollowerId()==follow.getFollowingId())
            throw new ResourceNotFoundException("cannot follow yourself");

        if (followRepository
                .findByFollowerIdAndFollowingId(follow.getFollowerId(), follow.getFollowingId())
                .isPresent())
            throw new ResourceNotFoundException("already followed");

        Follow savedFollow = followRepository.save(follow);
        return FollowMapper.mapToFollowDto(savedFollow);
    }

    @Transactional
    @Override
    public void unfollowUser(FollowDto followDto) {
        Long followerId = followDto.getFollowerId();
        Long followingId = followDto.getFollowingId();
        User follower = userRepository.findById(followerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
        User following = userRepository.findById(followingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
        followRepository.deleteByFollowerIdAndFollowingId(follower, following);
    }



}