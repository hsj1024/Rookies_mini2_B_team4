package com.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MainDto {
    private Long id;

    private String userId; // 작성자 ID 추가

    private String contents;
    private List<CommentDto> text;
    private List<PhotoDto> photos = new ArrayList<>();
    private int likes = 0;
    private String createdAt;
}
