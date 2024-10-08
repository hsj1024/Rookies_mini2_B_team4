package com.instagram.service.impl;

import com.instagram.dto.JoinDto;
import com.instagram.entity.User;
import com.instagram.repository.UserRepository;
import com.instagram.service.JoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.modelmapper.ModelMapper;

@Slf4j
@Service
public class JoinServiceImpl implements JoinService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public JoinServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public boolean joinProcess(JoinDto joinDto) {

		// 동일한 userId 확인 추가 -윤선
		if (userRepository.existsByUserId(joinDto.getUserId())) {
			log.warn("User with userId '{}' already exists", joinDto.getUserId());
			throw new IllegalArgumentException("이미 존재하는 사용자 ID 입니다.");
			//return false;
		}

		// 동일한 email 확인 추가 -윤선
		if (userRepository.existsByEmail(joinDto.getEmail())) {
			log.warn("User with email '{}' already exists", joinDto.getEmail());
			throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
			//return false;
		}

		// 동일한 userName 을 사용하고 있는지 확인
		if (userRepository.existsByUserName(joinDto.getUserName())) {
			log.warn("User with username '{}' already exists", joinDto.getUserName());
			//return false;
		}

		// 패스워드와 패스워드 확인이 일치하는지 확인
		if (!joinDto.checkPassword()) {
			log.warn("Password and password confirmation do not match for username '{}'", joinDto.getUserName());
			return false;
		}

		// JoinDto 의 값을 UserEntity 에 설정
		User user = new ModelMapper().map(joinDto, User.class);

		// 패스워드 암호화 처리 및 역할을 설정
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");   // 사용자 역할을 구분하는 문자로 "ROLE_" 접두어를 사용

		// UserEntity 를 저장
		try {
			userRepository.save(user);
			log.info("User '{}' has been successfully registered", user.getUserName());
			return true;
		} catch (Exception e) {
			log.error("Error occurred while saving user '{}': {}", user.getUserName(), e.getMessage());
			throw new RuntimeException("회원가입 중 오류가 발생했습니다.");
			//return false; 오류 메세지로 바꿈 - 윤선
		}
	}
}
