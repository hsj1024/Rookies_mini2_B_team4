package com.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PhotoDto {
    private Long id;

    private String userId;
    private String imageUrl;
    private String caption;
    private LocalDateTime createdAt;
}
