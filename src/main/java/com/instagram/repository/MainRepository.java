package com.instagram.repository;

import com.instagram.entity.Main;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainRepository extends JpaRepository<Main, Long> {

    List<Main> findByUserId(String userId);
    List<Main> findByUserIdIn(List<String> userIds);


}
