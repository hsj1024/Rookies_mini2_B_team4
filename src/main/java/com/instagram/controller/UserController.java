package com.instagram.controller;

import com.instagram.dto.FollowDto;
import com.instagram.dto.MainDto;
import com.instagram.dto.PhotoDto;
import com.instagram.dto.UserDto;
import com.instagram.entity.Photo;
import com.instagram.entity.User;
import com.instagram.exception.ResourceNotFoundException;
import com.instagram.repository.UserRepository;
import com.instagram.repository.PhotoRepository;
import com.instagram.service.FollowService;
import com.instagram.service.MainService;
import com.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MainService mainService;  // MainService 추가

    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

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
                                              @RequestParam("user") String userName,
                                              @RequestParam(value = "file",required = false) MultipartFile file){
        UserDto userDto = userService.updateUser(userId, userName, file);
        return ResponseEntity.ok(userDto);
    }
    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<UserDto>> getFriends(@PathVariable Long userId) {
        List<UserDto> friends = userService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/nameToId/{userId}")
    public ResponseEntity<Long> getUserByUserId(@PathVariable String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + userId)
                );
        return ResponseEntity.ok(user.getId());
    }

    @GetMapping("/profileImg/{userId}")
    public ResponseEntity<byte[]> getProfileImg(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + userId)
                );
        if (user.getProfileImage().isEmpty()) return null;
        else {
            Path imagePath = Paths.get(user.getProfileImage());

            if (!Files.exists(imagePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
            }
            try {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                return ResponseEntity.ok(imageBytes);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image file", e);
            }
        }
    }

    @GetMapping("/search/{search}")
    public ResponseEntity<List<User>> searchUser(@PathVariable String search) {
        List<User> users = userRepository.findByUserIdContainingOrUserNameContaining(search, search);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<ThumbnailDto> getPhotosByUserId(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + id)
                );
        List<Photo> photos = photoRepository.findFirstPhotosByUserId(id);
        List<Long> mainId = photos.stream()
                .map(photo -> photo.getMain().getId())
                .distinct() // Ensure unique mainIds
                .collect(Collectors.toList());

        if (photos.isEmpty()) return null;

        List<byte[]> photoBytesList = new ArrayList<>();

        for (Photo photo : photos) {
            try {
                Path imagePath = Paths.get(photo.getImageUrl());

                byte[] imageBytes = Files.readAllBytes(imagePath);

                photoBytesList.add(imageBytes);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image file", e);
            }
        }
        ThumbnailDto thumbnailDto = new ThumbnailDto(mainId,photoBytesList);
        return ResponseEntity.ok(thumbnailDto);

    }

}