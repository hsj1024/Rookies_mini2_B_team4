//package com.instagram.controller;
//
//import com.instagram.dto.JoinDto;
//import com.instagram.service.JoinService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class JoinController {
//	@Autowired
//	JoinService joinService;
//
//	@GetMapping("/join")
//	public String join() {
//		return "/join";
//	}
//
//	@PostMapping("/joinProc")
//	public String joinProc(@RequestBody JoinDto joinDto) {
//		if (joinService.joinProcess(joinDto)) {
//			return "redirect:/login";
//		} else {
//			return "redirect:/join";
//		}
//	}
//}

package com.instagram.controller;

import com.instagram.dto.JoinDto;
import com.instagram.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/join")
public class JoinController {

	@Autowired
	private JoinService joinService;

	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@RequestBody JoinDto joinDto) {
		boolean isRegistered = joinService.joinProcess(joinDto);
		if (isRegistered) {
			// 로그인 페이지로 리디렉션
			return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/api/login")).build();
		} else {
			return ResponseEntity.badRequest().build();
		}
	}


}