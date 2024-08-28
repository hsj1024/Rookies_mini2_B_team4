package com.instagram.service.impl;

import com.instagram.dto.FollowDto;
import com.instagram.dto.PhotoDto;
import com.instagram.dto.UserDto;
import com.instagram.dto.mapper.FollowMapper;
import com.instagram.dto.mapper.PhotoMapper;
import com.instagram.dto.mapper.UserMapper;
import com.instagram.entity.Follow;
import com.instagram.entity.Photo;
import com.instagram.entity.User;
import com.instagram.exception.ResourceNotFoundException;
import com.instagram.repository.FollowRepository;
import com.instagram.repository.PhotoRepository;
import com.instagram.repository.UserRepository;
import com.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PhotoRepository photoRepository;

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + id)
                );
        return UserMapper.mapToUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FollowDto> getFollower(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + userId)
                );;
        List<Follow> followers = followRepository.findByFollowingId(user);

        return followers.stream()
                .map(FollowMapper::mapToFollowDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<FollowDto> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + userId)
                );
        List<Follow> followings = followRepository.findByFollowerId(user);

        return followings.stream()
                .map(FollowMapper::mapToFollowDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Long getFollowerNum(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + userId)
                );
        return followRepository.countByFollowingId(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Long getFollowingNum(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + userId)
                );
        return followRepository.countByFollowerId(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PhotoDto> getPhotoByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not exists with a given id: " + userId)
                );
        List<Photo> photos = photoRepository.findByUserId(user);
        return photos.stream()
                .map(PhotoMapper::mapToPhotoDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long userId, String userName, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not exist")
                );
        //user.setUserName(updatedUser.getUserName());
        if (userName != null) {
            user.setUserName(userName);
        }
        String uploadDir = "profileImage/";
        if (file != null) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, file.getBytes());
                user.setProfileImage(filePath.toString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
            }
        }
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public List<UserDto> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFriends().stream()
                .map(friend -> new UserDto(friend.getId(), friend.getUserId(), friend.getUserName(), friend.getPassword(), friend.getEmail(), friend.getProfileImage()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Set<User> findUsersByStringIds(Set<String> userIds) {
        // Set<String> 타입의 userIds를 사용하여 User 엔티티를 조회하고 Set<User>로 반환
        return userIds.stream()
                .map(id -> userRepository.findByUserId(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with userId: " + id))
                )
                .collect(Collectors.toSet());
    }
//    public Optional<User> findUserById(Long id) {
//        return userRepository.findById(id);
//    }
    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 필요한 경우 User 엔티티를 UserDto로 변환
        return new UserDto(user.getId(), user.getUserName(), user.getEmail());
    }

//    @Override
//    public Set<User> findUsersByIds(Set<Long> ids) {
//        return ids.stream()
//                .map(this::findUserById)
//                .map(this::convertToUserEntity) // UserDto를 User로 변환
//                .collect(Collectors.toSet());
//    }

    @Transactional(readOnly = true)
    public Set<User> findUsersByIds(Set<String> userIds) {
        // UserRepository의 새로운 메서드를 사용하여 사용자 목록을 조회
        return new HashSet<>(userRepository.findByUserIdIn(userIds));
    }

    @Override
    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), new ArrayList<>());
    }

    // 채팅 구현 추가 - 서정
    @Override
    public boolean areFollowing(String currentUserId, String otherUserId) {
        User currentUser = userRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userId: " + currentUserId));

        User otherUser = userRepository.findByUserId(otherUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with userId: " + otherUserId));

        // 현재 사용자가 다른 사용자를 팔로우하고 있는지 확인
        return followRepository.existsByFollowerIdAndFollowingId(currentUser.getId(), otherUser.getId());
    }
}