package com.instagram.controller;

import com.instagram.common.JwtUtils;
import com.instagram.dto.CustomUserDetail;
import com.instagram.dto.LoginRequest;
import com.instagram.dto.LoginResponse;
import com.instagram.security.CustomUserDetailsService; // Import CustomUserDetailsService
import com.instagram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserService userService;  // 유저 정보를 가져오기 위한 서비스

	@Autowired
	private CustomUserDetailsService customUserDetailsService;  // CustomUserDetailsService 주입


//	@PostMapping("/login")
//	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
//		// userId를 기반으로 UserDetails를 가져옵니다.
//		UserDetails userDetails = customUserDetailsService.loadUserByUserId(loginRequest.getUserId());
//		// userDetails.getUsername()이 아닌 userDetails.getUserId()를 사용하여 인증을 수행합니다.
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword())
//		);
//
//
//
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		String jwt = jwtUtils.generateToken(authentication.getName());
//
//		return new LoginResponse(jwt);
//	}
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		// userId를 기반으로 UserDetails를 가져옵니다.
		UserDetails userDetails = customUserDetailsService.loadUserByUserId(loginRequest.getUserId());

		// userId를 사용하여 인증을 수행합니다.
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userDetails.getUsername(), loginRequest.getPassword())
		);

		// SecurityContext에 설정
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// CustomUserDetail로 캐스팅하여 userId를 가져옵니다.
		CustomUserDetail customUserDetail = (CustomUserDetail) userDetails;
		String jwt = jwtUtils.generateToken(customUserDetail.getUserId());

		return new LoginResponse(jwt);
	}

	@GetMapping("/login")
	public String getLoginPage() {
		return " 로그인은 백엔드에서 성공적으로 접속 완료했습니다. 프론트에서 로그인이 가능한지 확인하세요";
	}
}
