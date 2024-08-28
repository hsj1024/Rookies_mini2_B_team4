package com.instagram.dto.mapper;

import com.instagram.dto.MainDto;
import com.instagram.dto.PhotoDto;
import com.instagram.entity.Main;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MainMapper {

    public MainDto toDto(Main main){
        MainDto dto = new MainDto();
        dto.setId(main.getId());
        dto.setContents(main.getContents());
        dto.setLikes(main.getLikes());
        dto.setCreatedAt(main.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        //이미지 업로드 기능 구현 추가
        List<PhotoDto> photos = main.getPhotos().stream()
                .map(PhotoMapper::mapToPhotoDto)
                .collect(Collectors.toList());
        dto.setPhotos(photos);

        return dto;
    }

    public Main toEntity(MainDto mainDto){
        return new Main(
                mainDto.getId(),
                mainDto.getContents(),
                mainDto.getLikes()
        );
    }
}
