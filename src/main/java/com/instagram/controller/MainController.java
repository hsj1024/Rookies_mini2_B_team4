//
//package com.instagram.controller;
//
//import com.instagram.dto.MainDto;
//import com.instagram.service.MainService;
//import com.instagram.service.PhotoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/main")
//
//public class MainController {
//
//    @Autowired
//    private MainService mainService;
//    @Autowired
//    private PhotoService photoService;
//
//    // 모든 게시글 조회
//    @GetMapping
//    public ResponseEntity<List<MainDto>> getAllPosts() {
//        List<MainDto> posts = mainService.getAllPosts();
//        return ResponseEntity.ok(posts);  // JSON으로 반환
//    }
//
//    // 특정 게시글 ID로 조회
//    @GetMapping("/{id}")
//    public ResponseEntity<MainDto> getPostById(@PathVariable Long id) {
//        MainDto post = mainService.getPostById(id);
//        return ResponseEntity.ok(post);  // JSON으로 반환
//    }
//
//    // 게시글 등록
////    @PostMapping("/write")
////    public ResponseEntity<MainDto> createPost(@RequestBody MainDto mainDto) {
////        MainDto createdPost = mainService.createPost(mainDto);
////        return ResponseEntity.ok(createdPost);  // JSON으로 반환
////    }
//
//    // 보성님 이 부분 수정 중
//    // 게시글 등록 및 이미지 업로드
//    // 게시글 등록 및 이미지 업로드
//    @PostMapping("/write")
//    public ResponseEntity<MainDto> createPost(@RequestPart("post") MainDto mainDto,
//                                              @RequestPart(value = "files", required = false) List<MultipartFile> files,
//                                              @RequestHeader("User-Id") String userId) {
//
//        // 게시글을 먼저 생성
//        MainDto createdPost = mainService.createPost(mainDto);
//
//        // 파일이 존재하는지 확인하고 처리
//        if (files != null && !files.isEmpty()) {
//            for (MultipartFile file : files) {
//                // 업로드된 파일을 처리하고, Photo 엔티티로 변환하여 Main과 연결
//                photoService.uploadPhoto(file, createdPost.getId(), userId, mainDto.getPhotos().isEmpty() ? null : mainDto.getPhotos().get(0).getCaption());
//            }
//        }
//
//        return ResponseEntity.ok(createdPost);  // JSON으로 반환
//    }
//
//    // 게시글 수정
//    @PutMapping("/update/{id}")
//    public ResponseEntity<MainDto> updatePost(@PathVariable Long id, @RequestBody MainDto mainDto) {
//        MainDto updatedPost = mainService.updatePost(id, mainDto);
//        return ResponseEntity.ok(updatedPost);  // JSON으로 반환
//    }
//
//    // 게시글 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//        mainService.deletePost(id);
//        return ResponseEntity.noContent().build();  // 상태 코드 204 (No Content) 반환
//    }
//}

package com.instagram.controller;

import com.instagram.dto.MainDto;
import com.instagram.exception.UnauthorizedException;
import com.instagram.service.MainService;
import com.instagram.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/main")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MainService mainService;

    @Autowired
    private PhotoService photoService;

    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<MainDto>> getAllPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            // 인증되지 않은 사용자이거나 익명 사용자
            System.out.println("User is not authenticated or is anonymous");
        } else {
            String loggedInUserId = authentication.getName();
            System.out.println("Authenticated User ID: " + loggedInUserId);
        }
        String loggedInUserId = authentication.getName();

        List<MainDto> posts = mainService.getAllPosts(loggedInUserId);
        return ResponseEntity.ok(posts);
    }


    // 특정 게시글 ID로 조회
    @GetMapping("/{id}")
    public ResponseEntity<MainDto> getPostById(@PathVariable String id) {
        try {
            Long postId = Long.parseLong(id);
            MainDto post = mainService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    // 게시글 등록 및 이미지 업로드
    @PostMapping("/write")
    public ResponseEntity<MainDto> createPost(@RequestPart("post") MainDto mainDto,
                                              @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                              @RequestHeader(value = "User-Id", required = false) String userId) {
        // 만약 헤더로 userId를 전달받지 않았다면, 로그인된 사용자 정보 가져오기
        if (userId == null || userId.isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userId = authentication.getName();
        }

        // MainDto에 userId 설정
        mainDto.setUserId(userId);

        // 게시글 생성
        MainDto createdPost = mainService.createPost(mainDto);

        // 파일이 존재하는지 확인하고 처리
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                photoService.uploadPhoto(file, createdPost.getId(), userId,
                        mainDto.getPhotos().isEmpty() ? null : mainDto.getPhotos().get(0).getCaption());
            }
        }

        return ResponseEntity.ok(createdPost);
    }


    // 게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<MainDto> updatePost(@PathVariable Long id, @RequestBody MainDto mainDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName();

        try {
            MainDto updatedPost = mainService.updatePost(id, mainDto, loggedInUserId);
            return ResponseEntity.ok(updatedPost);
        } catch (UnauthorizedException e) {
            logger.error("Unauthorized access attempt by user: {}", loggedInUserId, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (RuntimeException e) {
            logger.error("Post not found with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Failed to update post with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName();

        try {
            mainService.deletePost(id, loggedInUserId);
            return ResponseEntity.noContent().build();
        } catch (UnauthorizedException e) {
            logger.error("Unauthorized access attempt by user: {}", loggedInUserId, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (RuntimeException e) {
            logger.error("Post not found with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Failed to delete post with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
