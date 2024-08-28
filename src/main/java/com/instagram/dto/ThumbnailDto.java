package com.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ThumbnailDto {
    private List<Long> mainId;
    private List<byte[]> imageBytes;

    public ThumbnailDto(List<Long> mainId, List<byte[]> imageBytes) {
        this.mainId = mainId;
        this.imageBytes = imageBytes;
    }
}