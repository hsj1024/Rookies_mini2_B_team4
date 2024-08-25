package com.instagram.dto.mapper;

import com.instagram.dto.PhotoDto;
import com.instagram.dto.UserDto;
import com.instagram.entity.Photo;
import com.instagram.entity.User;
import com.instagram.exception.ResourceNotFoundException;
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhotoMapper {

    private final UserRepository userRepository;

    public static PhotoDto mapToPhotoDto(Photo photo){
        return new PhotoDto(
                photo.getId(),
                photo.getUserId().getUserId(),  // userId는 String 타입의 사용자 ID를 사용
                photo.getImageUrl(),
                photo.getCaption(),
                photo.getCreatedAt()
        );
    }

    public Photo mapToPhoto(PhotoDto photoDto) {
        User user = userRepository.findByUserId(photoDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        return new Photo(
                photoDto.getId(),
                user,  // Long 타입의 기본 키를 갖는 User 엔티티를 사용
                photoDto.getImageUrl(),
                photoDto.getCaption(),
                photoDto.getCreatedAt()
        );
    }
}