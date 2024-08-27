package com.instagram.service.impl;

import com.instagram.dto.CommentDto;
import com.instagram.dto.MainDto;
import com.instagram.entity.Main;
import com.instagram.dto.mapper.MainMapper;
import com.instagram.repository.CommentRepository;
import com.instagram.repository.FollowRepository;
import com.instagram.repository.MainRepository;
import com.instagram.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private MainRepository mainRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MainMapper mainMapper;

    @Autowired
    private FollowRepository followRepository;

    @Override
    public List<MainDto> getAllPosts(String loggedInUserId) {
        // 로그인한 사용자가 팔로우한 사용자 목록을 가져옵니다.
        List<String> followingIds = followRepository.findFollowingIdsByFollowerId(loggedInUserId);

        // 자신의 글은 항상 포함되도록 합니다.
        followingIds.add(loggedInUserId);
        System.out.println("Following IDs: " + followingIds);
        // 팔로우한 사용자들의 게시글만 가져옵니다.
        List<Main> posts = mainRepository.findByUserIdIn(followingIds);
        System.out.println("Posts found: " + posts.size());
        return posts.stream()
                .map(this::mapToMainDtoWithComments) // 댓글 포함
                .collect(Collectors.toList());
    }
    @Override
    public MainDto getPostById(Long id) {
        Main post = mainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return mapToMainDtoWithComments(post); // 댓글 포함
    }
//    @Override
//    public MainDto getPostById(Long id) {
//        Main post = mainRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
//        return mapToMainDtoWithComments(post); // 댓글 포함
//    }

//    @Override
//    public MainDto getPostById(String id) {
//        Main post = mainRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
//        return mapToMainDtoWithComments(post); // 댓글 포함
//    }

    @Override
    public MainDto createPost(MainDto mainDto) {
        // 로그인된 사용자 정보 가져오기 (Spring Security)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName();

        // MainDto에 userId 설정
        mainDto.setUserId(loggedInUserId);

        // Main 엔티티로 변환 및 저장
        Main post = mainMapper.toEntity(mainDto);
        post.setUserId(loggedInUserId);  // userId가 null이 아니도록 설정
        Main savedPost = mainRepository.save(post);

        return mapToMainDtoWithComments(savedPost);
    }


    @Override
    public MainDto updatePost(Long id, MainDto mainDto) {
        Main existingPost = mainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        existingPost.setContents(mainDto.getContents());
        Main updatedPost = mainRepository.save(existingPost);
        return mapToMainDtoWithComments(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Main post = mainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        mainRepository.delete(post);
    }

    // Main Entity를 MainDto로 매핑하면서 댓글도 포함
    private MainDto mapToMainDtoWithComments(Main main) {
        MainDto mainDto = mainMapper.toDto(main);

        // Main에 연결된 댓글들을 가져와서 DTO로 변환
        List<CommentDto> comments = commentRepository.findByMainId(main.getId())
                .stream()
                .map(comment -> {
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(comment.getId());
                    commentDto.setText(comment.getText());
                    commentDto.setCreatedAt(comment.getCreatedAt().toString());
                    // 필요에 따라 다른 필드도 설정
                    return commentDto;
                })
                .collect(Collectors.toList());

        mainDto.setText(comments); // MainDto에 댓글 설정
        return mainDto;
    }
    @Override
    public List<MainDto> getPostsByUserId(String userId) {
        System.out.println("Fetching posts for userId: " + userId);

        List<Main> posts = mainRepository.findByUserId(userId);
        return posts.stream()
                .map(this::mapToMainDtoWithComments)
                .collect(Collectors.toList());
    }


}

