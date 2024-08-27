//JwtRequestFilter

//package com.instagram.security;
//
//import com.instagram.common.JwtUtils;
//import com.instagram.dto.CustomUserDetail;
//import com.instagram.entity.User;
//import com.instagram.repository.UserRepository;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@Slf4j
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//   @Autowired
//   private JwtUtils jwtUtils;
//
//   @Autowired
//   private UserRepository repository;
//
//   @Override
//   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//         throws ServletException, IOException {
//      String uri = request.getRequestURI();
//      if (uri.equals("/") || uri.equals("/login") || uri.equals("/api/loginProc") || uri.equals("/joinProc")
//            || uri.equals("/favicon.ico") || uri.equals("/api/main")) {
//         filterChain.doFilter(request, response);
//         return;
//      }
//
//      String jwtToken = null;
//      String subject = null;
//
//      String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//         jwtToken = authorizationHeader.substring(7);
//         try {
//            subject = jwtUtils.getSubjectFromToken(jwtToken);
//         } catch (Exception e) {
//            log.error("JWT 토큰 처리 중 예외 발생: " + e.getMessage());
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Invalid JWT token");
//            response.getWriter().flush();
//            return;
//         }
//      } else {
//         log.error("Authorization 헤더 누락 또는 토큰 형식 오류");
//         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//         response.getWriter().write("Invalid JWT token");
//         response.getWriter().flush();
//         return;
//      }
//
//      if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//         User user = repository.findByUserName(subject);
//
//         if (user != null && jwtUtils.validateToken(jwtToken, user)) {
//            CustomUserDetail userDetail = new CustomUserDetail(user);
//            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                  userDetail.getUsername(), null, userDetail.getAuthorities());
//
//            usernamePasswordAuthenticationToken
//                  .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//         } else {
//            log.error("JWT 토큰 검증 실패 또는 사용자 정보가 유효하지 않음");
//            SecurityContextHolder.getContext().setAuthentication(null);
//         }
//      }
//
//      filterChain.doFilter(request, response);
//   }
//}


package com.instagram.security;

import com.instagram.common.JwtUtils;
import com.instagram.dto.CustomUserDetail;
import com.instagram.entity.User;
import com.instagram.exception.ResourceNotFoundException;
import com.instagram.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserRepository repository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		if (uri.equals("/") || uri.equals("/api/login") || uri.equals("/api/loginProc") || uri.equals("/api/join/register")
				|| uri.equals("/favicon.ico") || uri.equals("/api/main") || uri.equals("/login") || uri.equals("/api/write")) {
			filterChain.doFilter(request, response);
			return;
		}
		if (uri.startsWith("/ws")) {
			filterChain.doFilter(request, response);
			return;
		}
		String jwtToken = null;
		String subject = null;

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
			try {
				subject = jwtUtils.getSubjectFromToken(jwtToken);
			} catch (Exception e) {
				log.warn("JWT 토큰 처리 중 예외 발생: {}", e.getMessage());  // 로그 레벨을 warn으로 변경
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid credentials");  // 일반적인 오류 메시지
				response.getWriter().flush();
				return;
			}
		} else {
			log.warn("Authorization 헤더 누락 또는 토큰 형식 오류");  // 로그 레벨을 warn으로 변경
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid credentials");  // 일반적인 오류 메시지
			response.getWriter().flush();
			return;
		}

		if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			User user = repository.findByUserId(subject)
					.orElseThrow(() ->
							new ResourceNotFoundException("User not exists")
					);

			if (user != null && jwtUtils.validateToken(jwtToken, user)) {
				CustomUserDetail userDetail = new CustomUserDetail(user);
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetail.getUserId(), null, userDetail.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			} else {
				log.warn("JWT 토큰 검증 실패 또는 사용자 정보가 유효하지 않음");  // 로그 레벨을 warn으로 변경
				SecurityContextHolder.getContext().setAuthentication(null);
			}
		}

		filterChain.doFilter(request, response);
	}
}

//