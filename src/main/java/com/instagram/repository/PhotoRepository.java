package com.instagram.repository;

import com.instagram.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository <Photo, Long> {
    List<Photo> findByMainId(Long mainId);
}
