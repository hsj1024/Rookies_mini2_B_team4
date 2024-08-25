//package com.instagram.runner;
//
//import com.instagram.entity.Follow;
//import com.instagram.entity.Photo;
//import com.instagram.entity.User;
//import com.instagram.repository.FollowRepository;
//import com.instagram.repository.PhotoRepository;
//import com.instagram.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@Profile("test")
//@RequiredArgsConstructor
//public class UserFollowInsertRunner implements ApplicationRunner {
//    final UserRepository userRepository;
//    final FollowRepository followRepository;
//    final PhotoRepository photoRepository;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        User user1 = new User();
//        user1.setUserId("amy");
//        user1.setUserName("에이미");
//        user1.setEmail("test@a.com");
//        user1.setProfileImage("amy.png");
//        user1.setPassword("0000");
//
//        User user2 = new User();
//        user2.setUserId("john");
//        user2.setUserName("존");
//        user2.setEmail("test2@a.com");
//        user2.setPassword("0000");
//
//        userRepository.saveAll(List.of(user1, user2));
//
//        Follow follow1 = new Follow();
//        follow1.setFollowerId(user1);
//        follow1.setFollowingId(user2);
//
//        followRepository.saveAll(List.of(follow1));
//
//        Photo photo1 = new Photo();
//        photo1.setUserId(user1);
//        photo1.setCaption("amy's photo");
//        photo1.setImageUrl("amy_photo.png");
//
//        photoRepository.saveAll(List.of(photo1));
//    }
//}
package com.instagram.runner;

import com.instagram.entity.Follow;
import com.instagram.entity.Photo;
import com.instagram.entity.User;
import com.instagram.repository.FollowRepository;
import com.instagram.repository.PhotoRepository;
import com.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFollowInsertRunner implements ApplicationRunner {
    final UserRepository userRepository;
    final FollowRepository followRepository;
    final PhotoRepository photoRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user1 = new User();
        user1.setUserId("amy");
        user1.setUserName("에이미");
        user1.setEmail("test@a.com");
        user1.setProfileImage("amy.png");
        user1.setPassword("0000");

        User user2 = new User();
        user2.setUserId("john");
        user2.setUserName("존");
        user2.setEmail("test2@a.com");
        user2.setPassword("0000");

        userRepository.saveAll(List.of(user1, user2));

        Follow follow1 = new Follow();
        follow1.setFollowerId(user1);
        follow1.setFollowingId(user2);

        followRepository.saveAll(List.of(follow1));

        Photo photo1 = new Photo();
        photo1.setUserId(user1);
        photo1.setCaption("amy's photo");
        photo1.setImageUrl("amy_photo.png");

        photoRepository.saveAll(List.of(photo1));
    }
}
