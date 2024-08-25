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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public UserDto updateUser(Long userId, UserDto updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not exist")
                );
        if (updatedUser.getUserName() != null)
            user.setUserName(updatedUser.getUserName());
        if (updatedUser.getProfileImage() != null)
            user.setProfileImage(updatedUser.getProfileImage());

        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
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
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}