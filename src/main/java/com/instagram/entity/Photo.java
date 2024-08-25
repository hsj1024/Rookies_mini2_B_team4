// 보성님 코드
//package com.instagram.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Table (name = "photo")
//public class Photo {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "user_id")
//    private String userId;
//
//    @Column(name = "image_url")
//    private String imageUrl;
//
//    @Column(name = "caption")
//    private String caption;
//
//    @Column(name = "created_at")
//    @CreatedDate
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @ManyToOne
//    private Main main;
//}

// 은영님 코드 추가

package com.instagram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "Photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "caption")
    private String caption;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private Main main;

    // 생성자 추가
    public Photo(Long id, User user, String imageUrl, String caption, LocalDateTime createdAt) {
        this.id = id;
        this.userId = user;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.createdAt = createdAt;
    }
}