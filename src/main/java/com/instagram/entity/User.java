package com.instagram.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String userId; // 회원가입 시 사용되는 사용자 ID
    private String password;
    // 기타 필요한 필드들
}
