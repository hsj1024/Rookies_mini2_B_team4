// 기존 코드
//package com.instagram.repository;
//
//import com.instagram.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface UserRepository extends JpaRepository<User, Long> {
//}

// 은영님 코드 추가
package com.instagram.repository;

import com.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

//public interface UserRepository extends JpaRepository<User, String> {
//    Optional<User> findByUserId(String userId);  // userId로 User를 조회하는 메서드
//
//}
public interface UserRepository extends JpaRepository<User, Long> {  // Long 타입의 기본 키
    Optional<User> findByUserId(String userId);  // String 타입의 userId로 User 조회
    //Optional<User> findById(String userId);

    // 윤선님 코드 추가
    boolean existsByUserName(String userName);  //username 중복 여부를 검사하는 쿼리 메서드
    // 사용자 정보 조회
    User findByUserName(String userName);
}