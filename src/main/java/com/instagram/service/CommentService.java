package com.instagram.service;

import com.instagram.dto.CommentDto;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto);
    CommentDto updateComment(Long id, CommentDto commentDto);
    void deleteComment(Long id);
    CommentDto getCommentById(Long id);
}
