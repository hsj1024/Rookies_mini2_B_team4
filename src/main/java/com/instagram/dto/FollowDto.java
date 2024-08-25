package com.instagram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowDto {
    private Long id;
    private Long followingId;
    private String followingUserId;
    private String followingUserName;
    private Long followerId;
    private String followerUserId;
    private String followerUserName;

}
