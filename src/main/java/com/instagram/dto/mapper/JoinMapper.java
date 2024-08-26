//package com.instagram.dto.mapper;
//
//import com.instagram.dto.JoinDto;
////import org.hibernate.mapping.Join;
//import com.instagram.entity.User;
//
//import jakarta.persistence.Column;
//
//public class JoinMapper {
//
//    public static JoinDto mapToJoinDto(User user) {
//        return new JoinDto(
//                user.getId(),
//                user.getUserId(),
//                user.getUserName(),
//                user.getPassword(),
//                user.getPasswordConfirm(),
//                user.getEmail(),
//                user.getProfileImage()
//        );
//    }
//
//    public static User mapToJoin(JoinDto joinDto) {
//        User user = new User();
//        user.setId(joinDto.getId());
//        user.setUserId(joinDto.getUser_id());
//        user.setUserName(joinDto.getUserName());
//        user.setPassword(joinDto.getPassword());
//        user.setPasswordConfirm(joinDto.getPasswordConfirm());
//        user.setEmail(joinDto.getEmail());
//        user.setProfileImage(joinDto.getProfileImage());
//        return user;
//    }
//}

package com.instagram.dto.mapper;

import com.instagram.dto.JoinDto;
import com.instagram.entity.User;

public class JoinMapper {

    public static JoinDto mapToJoinDto(User user) {
        return new JoinDto(
                user.getId(),
                user.getUserId(),
                user.getUserName(),
                user.getPassword(),
                user.getPasswordConfirm(),
                user.getEmail(),
                user.getProfileImage()
        );
    }

    public static User mapToJoin(JoinDto joinDto) {
        User user = new User();
        user.setId(joinDto.getId());
        user.setUserId(joinDto.getUserId());
        user.setUserName(joinDto.getUserName());
        user.setPassword(joinDto.getPassword());
        user.setPasswordConfirm(joinDto.getPasswordConfirm());
        user.setEmail(joinDto.getEmail());
        user.setProfileImage(joinDto.getProfileImage());
        return user;
    }
}
