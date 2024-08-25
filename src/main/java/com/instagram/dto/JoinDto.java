package com.instagram.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class JoinDto {

    private Long id;
    private String user_id;
    private String userName;
    private String password;
    private String passwordConfirm;
    private String email;
    private String profileImage;

    public JoinDto(Long id, String user_id,String userName,String password, String passwordConfirm, String email
            , String profileImage) {
        this.id = id;
        this.user_id = user_id;
        this.userName = userName;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.email = email;
        this.profileImage = profileImage;
    }

    // 패스워드와 패스워드 확인이 동일한지 확인
    public boolean checkPassword() {
        return this.password != null && this.password.equals(this.passwordConfirm);
    }
}