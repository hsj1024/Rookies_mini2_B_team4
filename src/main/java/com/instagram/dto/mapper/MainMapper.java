package com.instagram.dto.mapper;

import com.instagram.dto.MainDto;
import com.instagram.entity.Main;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
@Component
public class MainMapper {
    public MainDto toDto(Main main){
        MainDto dto = new MainDto();
        dto.setId(main.getId());
        dto.setContents(main.getContents());
        dto.setLikes(main.getLikes());
        dto.setCreatedAt(main.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
