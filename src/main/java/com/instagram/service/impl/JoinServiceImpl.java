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
	
	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public JoinServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	public boolean joinProcess(JoinDto joinDto) {
		// 동일한 username 을 사용하고 있는지 확인
		if (userRepository.existsByUserName(joinDto.getUserName()))
			return false;
		
		// 패스워드와 패스워드 확인이 일치하는지 확인
		if (!joinDto.checkPassword())
			return false;
		
		// JoinDto 의 값을 UserEntity 에 설정
		User user = new ModelMapper().map(joinDto, User.class);

		// 패스워드 암호화 처리 및 역할을 설정
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");	// 사용자 역할을 구분하는 문자로 "ROLE_" 접두어를 사용
		
		// UserEntity 를 저장
		try {
			userRepository.save(user);
			log.debug("레포지토리 오류");
			return true;
		} catch (Exception e) {
			return false;	
		}
	}
}