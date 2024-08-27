package com.instagram.controller;

import com.instagram.dto.FollowDto;
import com.instagram.dto.MainDto;
import com.instagram.dto.PhotoDto;
import com.instagram.dto.UserDto;
import com.instagram.entity.User;
import com.instagram.exception.ResourceNotFoundException;
import com.instagram.repository.UserRepository;
import com.instagram.service.FollowService;
import com.instagram.service.MainService;
import com.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MainService mainService;  // MainService 추가


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
                                              @RequestParam("file")MultipartFile file){
        UserDto userDto = userService.updateUser(userId, userName, file);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<UserDto>> getFriends(@PathVariable Long userId) {
        List<UserDto> friends = userService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/nameToId/{userName}")
    public ResponseEntity<Long> getUserByUserId(@PathVariable String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + userId)
                );
        return ResponseEntity.ok(user.getId());
    }

    // 사용자의 게시글 보여주기 위해 내 정보 가져오기 - 보성님 기능
//    @GetMapping("/{userId}/posts")
//    public ResponseEntity<List<MainDto>> getPostsByUserId(@PathVariable String userId) {
//        try {
//            List<MainDto> userPosts = mainService.getPostsByUserId(userId);
//            return ResponseEntity.ok(userPosts);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if not found
//        } catch (Exception e) {
//            // Log the error for debugging
//            System.out.println("An error occurred: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return 500 for other errors
//        }
//    }

}