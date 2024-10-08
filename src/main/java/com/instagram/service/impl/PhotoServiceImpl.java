package com.instagram.service.impl;

import com.instagram.dto.PhotoDto;
import com.instagram.entity.Main;
import com.instagram.entity.Photo;
import com.instagram.entity.User;
import com.instagram.repository.MainRepository;
import com.instagram.repository.PhotoRepository;
import com.instagram.repository.UserRepository;
import com.instagram.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private MainRepository mainRepository;

    @Autowired
    private UserRepository userRepository; // UserRepository
    private final String uploadDir = "uploads/"; // 파일을 임시로 저장할 디렉토리 경로

    @Override
    public PhotoDto uploadPhoto(MultipartFile file, Long mainId, String userId, String caption) {
        try {
            // 파일을 저장
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // Photo 엔티티 생성 및 저장
            Photo photo = new Photo();
            // User 찾기
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            photo.setUserId(user);
            photo.setImageUrl(filePath.toString());
            photo.setCaption(caption);

            // Main 찾기
            Main main = mainRepository.findById(mainId)
                    .orElseThrow(() -> new RuntimeException("Main post not found with id: " + mainId));
            photo.setMain(main);

            Photo savedPhoto = photoRepository.save(photo);

            // PhotoDto로 변환하여 반환
            return toDto(savedPhoto);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    private PhotoDto toDto(Photo photo) {
        PhotoDto dto = new PhotoDto();
        dto.setId(photo.getId());
        dto.setUserId(photo.getUserId().getId().toString());
        dto.setImageUrl(photo.getImageUrl());
        dto.setCaption(photo.getCaption());
        dto.setCreatedAt(photo.getCreatedAt());
        return dto;
    }

    public ResponseEntity<Resource> getImage(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // MIME 타입을 MediaType으로 변환
                String contentType = Files.probeContentType(filePath);
                MediaType mediaType = MediaType.parseMediaType(contentType);

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
