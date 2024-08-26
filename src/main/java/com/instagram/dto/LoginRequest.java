package com.instagram.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
    private String userId;  // userId 필드 추가



}
