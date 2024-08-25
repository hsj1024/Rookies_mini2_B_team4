package com.instagram.controller;

import com.instagram.dto.FollowDto;
import com.instagram.dto.PhotoDto;
import com.instagram.dto.UserDto;
import com.instagram.service.FollowService;
import com.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable("id") Long id){
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{userId}/photos")
    public ResponseEntity<List<PhotoDto>> getPhotoByUserId(@PathVariable("userId") Long userId){
        List<PhotoDto> photos = userService.getPhotoByUserId(userId);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("/{userId}/follower")
    public ResponseEntity<List<FollowDto>> getFollower(@PathVariable Long userId) {
        List<FollowDto> followers = userService.getFollower(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/follower_num")
    public ResponseEntity<Long> getFollowerNum(@PathVariable Long userId) {
        Long followerNum = userService.getFollowerNum(userId);
        return ResponseEntity.ok(followerNum);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowDto>> getFollowing(@PathVariable Long userId) {
        List<FollowDto> followings = userService.getFollowing(userId);
        return ResponseEntity.ok(followings);
    }

    @GetMapping("/{userId}/following_num")
    public ResponseEntity<Long> getFollowingNum(@PathVariable Long userId) {
        Long followingNum = userService.getFollowingNum(userId);
        return ResponseEntity.ok(followingNum);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId,
                                              @RequestBody UserDto updatedUser){
        UserDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userDto);
    }

}