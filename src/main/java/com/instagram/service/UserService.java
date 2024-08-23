//package com.rookies_talk.service;
//
//import com.rookies_talk.entity.User;
//import com.rookies_talk.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class UserService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public User createUser(User user) {
//        return userRepository.save(user);
//    }
//    public User findUserById(Long id) {
//        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
//    }
//    public Set<User> findUsersByIds(Set<Long> userIds) {
//        return new HashSet<>(userRepository.findAllById(userIds));
//    }
//}
package com.instagram.service;

import com.instagram.entity.User;
import com.instagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Set<User> findUsersByIds(Set<Long> userIds) {
        return new HashSet<>(userRepository.findAllById(userIds));
    }
    public User createUser(User user) {
            return userRepository.save(user);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // 사용자 ID로 사용자를 조회하는 메서드 추가
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("User not found with ID " + id));
    }
}
