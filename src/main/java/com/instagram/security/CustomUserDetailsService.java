package com.instagram.security;

import com.instagram.dto.CustomUserDetail;
import com.instagram.entity.User;
import com.instagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 유저를 데이터베이스에서 검색
		User user = userRepository.findByUserName(username);

		// 유저가 존재하지 않으면 예외 발생
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		// 유저 정보를 담은 CustomUserDetail 객체를 반환
		return new CustomUserDetail(user);
	}
}
