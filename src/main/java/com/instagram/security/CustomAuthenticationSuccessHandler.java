package com.instagram.security;

import com.instagram.common.JwtUtils;
import com.instagram.entity.User;
import com.instagram.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Environment env;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		User user = userRepository.findByUserName(userDetails.getUsername());

		// 로그인 성공 후 수행할 작업들을 기술

		String jwtToken = jwtUtils.generateToken(user);
		log.debug("Generated JWT Token: " + jwtToken);

		// 세션에 사용자 정보를 저장
		request.getSession().setAttribute("user", user);

		// 응답 헤더에 생성한 토큰을 설정
		response.setHeader("Authorization", "Bearer " + jwtToken);  // 토큰을 Authorization 헤더에 설정

		// 추가로  리다이렉션 또는 JSON 응답을 처리 할수..? 있음
		// 예시로 넣은 부분 :  JSON 응답을 반환
		response.setContentType("application/json");
		response.getWriter().write("{\"token\":\"" + jwtToken + "\"}");
		response.getWriter().flush();
	}
}