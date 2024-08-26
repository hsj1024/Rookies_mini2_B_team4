//package com.instagram.config;
//
//import com.instagram.security.CustomUserDetailsService;
//import com.instagram.security.JwtRequestFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//
//	private final CustomUserDetailsService userDetailsService;
//	private final JwtRequestFilter jwtRequestFilter;
//
//	public SecurityConfiguration(CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
//		this.userDetailsService = userDetailsService;
//		this.jwtRequestFilter = jwtRequestFilter;
//	}
//
//	@Bean
//	public static BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//				.authorizeHttpRequests(auth -> auth
//						.requestMatchers("/api/login", "/api/main", "/ws/**", "/api/main/write", "api/join/register").permitAll()
//						.anyRequest().authenticated()
//				)
//				.csrf(csrf -> csrf.disable()) // REST API의 경우 CSRF 보호를 비활성화하는 것이 일반적
//				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
//
//		        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 관리 비활성화 (JWT 토큰 사용 시)
//
//
//		return http.build();
//	}
//
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}
//}
package com.instagram.config;

import com.instagram.security.CustomUserDetailsService;
import com.instagram.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private final CustomUserDetailsService userDetailsService;
	private final JwtRequestFilter jwtRequestFilter;

	public SecurityConfiguration(CustomUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtRequestFilter = jwtRequestFilter;
	}

	@Bean
	public static BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors // CORS 설정 추가
						.configurationSource(request -> {
							CorsConfiguration config = new CorsConfiguration();
							config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // React 앱의 도메인을 허용
							config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
							config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
							config.setAllowCredentials(true);
							return config;
						})
				)
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/login", "/api/main", "/ws/**", "/api/main/write", "api/join/register").permitAll()
						.anyRequest().authenticated()
				)
				.csrf(csrf -> csrf.disable()) // REST API의 경우 CSRF 보호를 비활성화하는 것이 일반적
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 관리 비활성화 (JWT 토큰 사용 시)


		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
