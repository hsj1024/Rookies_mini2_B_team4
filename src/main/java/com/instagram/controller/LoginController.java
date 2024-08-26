package com.instagram.controller;

import com.instagram.common.JwtUtils;
import com.instagram.dto.LoginRequest;
import com.instagram.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateToken(authentication.getName());

		return new LoginResponse(jwt);
	}
	@GetMapping("/login")
	public String getLoginPage() {
		return " 로그인은 백엔드에서 성공적으로 접속 완료했습니다. 프론트에서 로그인이 가능한지 확인하세요";
	}
}
