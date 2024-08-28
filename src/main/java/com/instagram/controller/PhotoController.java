package com.instagram.controller;

import com.instagram.dto.PhotoDto;
import com.instagram.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/upload")
    public PhotoDto uploadPhoto(@RequestParam("file") MultipartFile file,
                                @RequestParam("mainId") Long mainId,
                                @RequestParam("userId") String userId,
                                @RequestParam("caption") String caption) {
        return photoService.uploadPhoto(file, mainId, userId, caption);
    }

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
            Path filePath = fileStorageLocation.resolve(filename).normalize();

            if (Files.exists(filePath)) {
                byte[] imageBytes = Files.readAllBytes(filePath);
                return ResponseEntity.ok(imageBytes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error retrieving file " + filename, e);
        }
    }
}
