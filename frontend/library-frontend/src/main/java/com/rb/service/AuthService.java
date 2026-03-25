package com.rb.service;

import com.rb.exception.UserException;
import com.rb.paylode.dto.UserDTO;
import com.rb.paylode.response.AuthResponse;

public interface AuthService {
	AuthResponse login(String username,String password) throws UserException;
	AuthResponse signup(UserDTO req) throws UserException;
	
	void createPasswordResetToken(String email) throws UserException;
	void resetPassword(String token,String newPassword) throws UserException;
}
