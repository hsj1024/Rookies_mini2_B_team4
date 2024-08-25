// 기존 코드
//package com.instagram.entity;
//
//import lombok.Data;
//import jakarta.persistence.*;
//
//@Entity
//@Data
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String username;
//    private String userId; // 회원가입 시 사용되는 사용자 ID
//    private String password;
//    // 기타 필요한 필드들
//}

package com.instagram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table (name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "role")
    private String role;  // 역할

    @Transient
    private String passwordConfirm;

    // 필요한 모든 필드를 포함하는 생성자
    public User(Long id, String userId, String userName, String email, String password, String profileImage) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
    }

}