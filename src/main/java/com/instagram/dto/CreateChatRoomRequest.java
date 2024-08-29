package com.instagram.dto;

import lombok.Data;

import java.util.Set;
@Data
public class CreateChatRoomRequest {
    private String name;
    private Set<Long> users;  // User ID 목록을 받을 수 있도록 Long 타입으로 정의

//    // Getters and Setters
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Set<Long> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<Long> users) {
//        this.users = users;
//    }
}
