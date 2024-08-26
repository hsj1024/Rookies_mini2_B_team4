//package com.rookies_talk.service;
//
//import com.rookies_talk.entity.User;
//import com.rookies_talk.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public User createUser(User user) {
//        return userRepository.save(user);
//    }
//    public User findUserById(Long id) {
//        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//    }
//    public Set<User> findUsersByIds(Set<Long> userIds) {
//        return new HashSet<>(userRepository.findAllById(userIds));
//    }
//}
package com.instagram.service;

import com.instagram.dto.FollowDto;
import com.instagram.dto.PhotoDto;
import com.instagram.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserDto getUserById(Long id);

    List<FollowDto> getFollower(Long userid);

    List<FollowDto> getFollowing(Long userid);

    Long getFollowerNum(Long userId);

    Long getFollowingNum(Long userId);

    List<PhotoDto> getPhotoByUserId(Long userId);

    UserDto updateUser(Long userId, UserDto updatedUser, MultipartFile file);

    List<UserDto> getFriends(Long userId);

}