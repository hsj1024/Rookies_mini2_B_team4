package com.instagram.security;

import com.instagram.dto.CustomUserDetail;
import com.instagram.entity.User;
import com.instagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> users = userRepository.findByUserName(username);

		if (users.isEmpty()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		if (users.size() > 1) {
			// 다중 결과가 반환될 때 처리 로직 추가 (예: 첫 번째 결과를 사용)
			return new CustomUserDetail(users.get(0)); // 첫 번째 사용자 반환
		}

		return new CustomUserDetail(users.get(0));
	}


	public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		// 유저를 데이터베이스에서 userId로 검색
		User user = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

		return new CustomUserDetail(user);
	}
}
