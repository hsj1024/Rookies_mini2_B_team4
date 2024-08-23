package com.instagram.service;

import com.instagram.dto.PhotoDto;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    PhotoDto uploadPhoto(MultipartFile file, Long mainId, String userId, String caption);
}
