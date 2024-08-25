package com.instagram.controller;

import com.instagram.dto.JoinDto;
import com.instagram.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JoinController {
	@Autowired
	JoinService joinService;
	
	@GetMapping("/join")
	public String join() {
		return "/join";
	}
	
	@PostMapping("/joinProc")
	public String joinProc(@RequestBody JoinDto joinDto) {
		if (joinService.joinProcess(joinDto)) {
			return "redirect:/login";
		} else {
			return "redirect:/join";
		}
	}
}