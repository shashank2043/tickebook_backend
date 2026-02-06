package org.team11.tickebook.auth_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.team11.tickebook.auth_service.dto.OtpRequest;
import org.team11.tickebook.auth_service.service.UserService;
import org.team11.tickebook.auth_service.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService service;
	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(service.getUser(id));
	}
	@PostMapping("/verify")
	public ResponseEntity<?> generateOtp(Authentication authentication) {
		String email = authentication.getName(); // comes from JWT
		System.out.println(email);
		service.generateOtp(email);
		return ResponseEntity.status(HttpStatus.CREATED).body("Otp sent to the email address: "+email);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(
			Authentication authentication,
			@RequestBody OtpRequest req) {
		String email = authentication.getName();
		service.validateOtp(email, req.getOtp());
		return ResponseEntity.ok("Otp verified");
	}
}
