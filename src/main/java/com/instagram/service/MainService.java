package com.instagram.service;

import com.instagram.dto.MainDto;

import java.util.List;

public interface MainService {
    List<MainDto> getAllPosts();

    MainDto getPostById(Long id);

    MainDto createPost(MainDto mainDto);

    MainDto updatePost(Long id, MainDto mainDto);

    void deletePost(Long id);

    // 특정 사용자의 게시글을 가져오는 메서드
    List<MainDto> getPostsByUserId(String userId);
}
