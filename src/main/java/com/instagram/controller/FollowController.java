package com.instagram.controller;

import com.instagram.dto.FollowDto;
import com.instagram.dto.mapper.FollowMapper;
import com.instagram.entity.Follow;
import com.instagram.entity.User;
import com.instagram.exception.ResourceNotFoundException;
import com.instagram.repository.FollowRepository;
import com.instagram.repository.UserRepository;
import com.instagram.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;
    private FollowRepository followRepository;
    private UserRepository userRepository;

    private FollowMapper followMapper;

    @PostMapping
    public ResponseEntity<FollowDto> followUser(@RequestBody FollowDto followDto) {
        FollowDto follow = followService.followUser(followDto);
        return new ResponseEntity<>(follow, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<String> unfollowUser(@RequestBody FollowDto followDto) {
        followService.unfollowUser(followDto);
        return ResponseEntity.ok("unfollowed successfully!.");
    }

    // 팔로우 친구 목록 가져오기 - 서정
//    @GetMapping("/following/{userId}")
//    public ResponseEntity<List<FollowDto>> getFollowing(@PathVariable Long userId) {
//        List<FollowDto> followingList = followService.getFollowing(userId);
//        return ResponseEntity.ok(followingList);
//    }






}