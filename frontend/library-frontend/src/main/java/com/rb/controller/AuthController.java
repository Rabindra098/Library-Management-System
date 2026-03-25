package com.rb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rb.exception.UserException;
import com.rb.paylode.dto.UserDTO;
import com.rb.paylode.request.ForgotPasswordRequest;
import com.rb.paylode.request.LoginRequest;
import com.rb.paylode.request.ResetPasswordRequest;
import com.rb.paylode.response.APIResponse;
import com.rb.paylode.response.AuthResponse;
import com.rb.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signupHandler(@Valid @RequestBody  UserDTO req) throws UserException {
		AuthResponse res=authService.signup(req);
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginHandler(@Valid @RequestBody  LoginRequest req) throws UserException {
		AuthResponse res=authService.login(req.getEmail(),req.getPassword());
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<APIResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) throws UserException {

	    authService.createPasswordResetToken(request.getEmail());
	    APIResponse res = new APIResponse("A Reset link was sent to your email.",true);
	    return ResponseEntity.ok(res);
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<APIResponse> resetPassword(@RequestBody ResetPasswordRequest request) throws Exception {
	    authService.resetPassword(
	            request.getToken(),
	            request.getPassword()
	    );
	    APIResponse res = new APIResponse("Password reset successful",true);

	    return ResponseEntity.ok(res);
	}
}
