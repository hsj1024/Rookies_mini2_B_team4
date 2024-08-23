package com.instagram.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PhotoDto {
    private Long id;
    private String userId;
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;
}
