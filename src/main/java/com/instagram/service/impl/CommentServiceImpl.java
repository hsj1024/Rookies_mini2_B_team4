package com.instagram.service.impl;

import com.instagram.dto.CommentDto;
import com.instagram.entity.Comment;
import com.instagram.entity.Main;
import com.instagram.repository.CommentRepository;
import com.instagram.repository.MainRepository;
import com.instagram.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MainRepository mainRepository;

    // 댓글 생성
    @Override
    public CommentDto createComment(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setPhotoId(commentDto.getPhotoId());
        comment.setText(commentDto.getText());

        // Main 객체 설정
        Main main = mainRepository.findById((long) commentDto.getMain())
                .orElseThrow(() -> new RuntimeException("Main post not found with id: " + commentDto.getMain()));
        comment.setMain(main);  // Main 객체를 Comment에 설정


        Comment savedComment = commentRepository.save(comment);
        return toDto(savedComment);
    }

    // 댓글 수정
    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setText(commentDto.getText());
            return toDto(commentRepository.save(comment));
        }
        return null;
    }

    // 특정 id 댓글 삭제
    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // 특정 id 댓글 조회
    @Override
    public CommentDto getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(this::toDto).orElse(null);
    }

    // Comment Entity -> CommentDto
    private CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPhotoId(comment.getPhotoId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt().toString());
        // Main 객체가 null이 아닌지 확인
        if (comment.getMain() != null) {
            dto.setMain(comment.getMain().getId().intValue());
        } else {
            dto.setMain(0); // 기본값 또는 에러 처리
        }
        return dto;
    }
}
