package com.instagram.controller;

import com.instagram.dto.MainDto;
import com.instagram.service.MainService;
import com.instagram.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/main")
public class MainController {

    @Autowired
    private MainService mainService;
    @Autowired
    private PhotoService photoService;

    // 모든 게시글 조회
    @GetMapping
    public List<MainDto> getAllPosts() {
        return mainService.getAllPosts();
    }

    // 특정 게시글 ID로 조회
    @GetMapping("/{id}")
    public MainDto getPostById(@PathVariable Long id) {
        return mainService.getPostById(id);
    }

    // 게시글 등록
    @PostMapping("/write")
    public MainDto createPost(@RequestBody MainDto mainDto) {
        return mainService.createPost(mainDto);
    }
//    //게시글 등록 및 이미지 업로드
//    @PostMapping("/write")
//    public MainDto createPost(@RequestPart("post") MainDto mainDto,
//                              @RequestPart("files") List<MultipartFile> files) {
//
//        // 게시글을 먼저 생성
//        MainDto createdPost = mainService.createPost(mainDto);
//
//        // 업로드된 파일을 처리하고, Photo 엔티티로 변환하여 Main과 연결
//        for (MultipartFile file : files) {
//            photoService.uploadPhoto(file, createdPost.getId(), mainDto.getUserId(), mainDto.getCaption());
//        }
//
//        return createdPost;
//    }

    // 게시글 수정
    @PutMapping("/update/{id}")
    public MainDto updatePost(@PathVariable Long id, @RequestBody MainDto mainDto){
        return mainService.updatePost(id, mainDto);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public MainDto deletePost(@PathVariable Long id){
        mainService.deletePost(id);
        return null;
    }
}

