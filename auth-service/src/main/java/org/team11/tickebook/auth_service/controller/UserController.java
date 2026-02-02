package org.team11.tickebook.auth_service.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team11.tickebook.auth_service.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;
	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable UUID id){
		return ResponseEntity.status(HttpStatus.OK).body(service.getUser(id));
	}

}
