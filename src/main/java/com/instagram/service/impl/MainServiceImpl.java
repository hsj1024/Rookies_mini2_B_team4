package com.instagram.service.impl;

import com.instagram.dto.CommentDto;
import com.instagram.dto.MainDto;
import com.instagram.entity.Main;
import com.instagram.dto.mapper.MainMapper;
import com.instagram.repository.CommentRepository;
import com.instagram.repository.MainRepository;
import com.instagram.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<MainDto> getAllPosts() {
        List<Main> posts = mainRepository.findAll();
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

    @Override
    public MainDto createPost(MainDto mainDto) {
        Main post = mainMapper.toEntity(mainDto);
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
}

