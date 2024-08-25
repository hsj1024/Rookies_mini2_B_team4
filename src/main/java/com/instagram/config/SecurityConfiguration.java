//package com.instagram.config;
//
//import com.instagram.security.CustomAuthenticationSuccessHandler;
//import com.instagram.security.JwtRequestFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration {
//
//   @Autowired
//   private JwtRequestFilter jwtRequestFilter;
//
//   @Autowired
//   private CustomAuthenticationSuccessHandler successHandler;
//
//   @Bean
//   SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//      http
//            .authorizeHttpRequests((auth) -> auth
//                  .requestMatchers("/", "/login", "/home", "/join", "/joinProc", "/api/loginProc", "/favicon.ico").permitAll()
//                  .requestMatchers("/admin").hasRole("ADMIN")
//                  .requestMatchers("/board/**", "/api/**").hasAnyRole("ADMIN", "USER")
//                  .anyRequest().authenticated()
//            )
//            .formLogin((auth) -> auth
//                  .loginProcessingUrl("/api/loginProc")
//                  .permitAll()
//                  .successHandler(successHandler)
//            )
//            .csrf((auth) -> auth.disable())  // CSRF를 필요 시 활성화
//            .logout((auth) -> auth
//                  .logoutUrl("/logout")
//                  .logoutSuccessUrl("/")
//                  .invalidateHttpSession(true)
//                  .deleteCookies("JSESSIONID")  // 로그아웃 시 세션과 쿠키 제거
//            )
//            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//      return http.build();
//   }
//
//   @Bean
//   BCryptPasswordEncoder bCryptPasswordEncoder() {
//      return new BCryptPasswordEncoder();
//   }
//
//
//}

package com.instagram.config;

import com.instagram.security.CustomAuthenticationSuccessHandler;
import com.instagram.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;

	@Autowired
	private CorsConfigurationSource corsConfigurationSource; // CorsConfig에서 정의된 Bean을 주입

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource)) // 새로운 방식으로 CORS 설정
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/home","/api/main", "/join", "/joinProc", "/api/loginProc", "/favicon.ico").permitAll()
						.requestMatchers("/admin").hasRole("ADMIN")
						.requestMatchers("/board/**", "/api/**").hasAnyRole("ADMIN", "USER")
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginProcessingUrl("/api/loginProc")
						.permitAll()
						.successHandler(successHandler)
				)
				.csrf(csrf -> csrf.disable())  // 필요 시 CSRF 활성화
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")  // 로그아웃 시 세션과 쿠키 삭제
				)
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
