package com.rb.service;

import java.util.List;

import com.rb.model.User;
import com.rb.paylode.dto.UserDTO;

public interface UserService {
	public User getCurrentUser() throws Exception;
	public List<UserDTO> getAllUser();
    User findById(Long id) throws Exception;
}
