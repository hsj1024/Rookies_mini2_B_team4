package com.instagram.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		
		return "redirect:/api/main"; // SecurityConfiguration 에서 설정한 logoutSuccessUrl 값 보다 우선
	}
}