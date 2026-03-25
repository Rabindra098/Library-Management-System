package com.rb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rb.model.User;
import com.rb.paylode.dto.UserDTO;
import com.rb.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfile() throws Exception {
		return ResponseEntity.ok(userService.getCurrentUser());
	}
	

	@GetMapping("/list")
	public ResponseEntity<List<UserDTO>> getAllUser() {
		return ResponseEntity.ok(userService.getAllUser());
	}
	
	
}
