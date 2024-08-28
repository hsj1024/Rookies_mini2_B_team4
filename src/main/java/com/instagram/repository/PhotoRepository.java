// 기존 코드
//package com.instagram.repository;
//
//import com.instagram.entity.Photo;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface PhotoRepository extends JpaRepository <Photo, Long> {
//    List<Photo> findByMainId(Long mainId);
//}

package com.instagram.repository;  // 패키지 이름은 통일된 이름으로 설정

import com.instagram.entity.Photo;
import com.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // User 엔티티를 사용하여 사진을 찾는 메서드
    List<Photo> findByUserId(User user);

    // Main 엔티티의 ID를 사용하여 사진을 찾는 메서드
    List<Photo> findByMainId(Long mainId);

    @Query("SELECT p FROM Photo p WHERE p.userId.id = :user AND p.createdAt IN " +
            "(SELECT MIN(p2.createdAt) FROM Photo p2 GROUP BY p2.main)")
    List<Photo> findFirstPhotosByUserId(@Param("user") Long user);
}
