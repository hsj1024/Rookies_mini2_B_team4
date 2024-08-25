package com.instagram.controller;

import com.instagram.dto.FollowDto;
import com.instagram.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

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


}