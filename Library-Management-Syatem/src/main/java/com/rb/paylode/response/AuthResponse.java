package com.rb.paylode.response;

import com.rb.paylode.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	private String jwt;
	private String message;
	private String title;
	private UserDTO userDTO;
}
