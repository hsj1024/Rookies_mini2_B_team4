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
import java.util.Set;

//public interface UserRepository extends JpaRepository<User, String> {
//    Optional<User> findByUserId(String userId);  // userId로 User를 조회하는 메서드
//
//}
public interface UserRepository extends JpaRepository<User, Long> {  // Long 타입의 기본 키
    Optional<User> findByUserId(String userId);  // userId 필드로 User 조회
    List<User> findByUserName(String userName);

    // 윤선님 코드 추가
    boolean existsByUserName(String userName);  //username 중복 여부를 검사하는 쿼리 메서드
    // 사용자 정보 조회
    //User findByUserName(String userName);

    // 채팅 기능 위해 추가 - 서정
    List<User> findByUserIdIn(Set<String> userIds);


    // userId, Email boolean 추가 - 윤선
    boolean existsByUserId(String userId);
    boolean existsByEmail(String Email);

    // 은영님 추가
    List<User> findByUserIdContainingOrUserNameContaining(String userId, String userName);


}