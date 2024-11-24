package com.spring.security.safeguardspring.web;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebController {


	@GetMapping("/")
	public String home() {
		return ("<h1>Welcome</h1>");
	}

	@GetMapping("/getAuthManager")
	public ResponseEntity<?> getAuthenticationManager() {
		return new ResponseEntity<>(SecurityContextHolder.getContext(), HttpStatus.OK);
	}

	@GetMapping("/user")
	public String user() {
		return ("<h1>Welcome User</h1>");
	}

	@GetMapping("/admin")
	public String admin() {
		return ("<h1>Welcome Admin</h1>");
	}
}
