package com.rb.service.impl;

import com.rb.model.User;
import com.rb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomeUserServiceImpl implements UserDetailsService{
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByEmail(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("USer Not exist with username: "+username);
		}
		
		GrantedAuthority authority=new SimpleGrantedAuthority(user.getRole().toString());
		
		Collection<? extends GrantedAuthority> authorities=Collections.singletonList(authority);
		
		return new org.springframework.security.core.userdetails
				.User(user.getEmail(),user.getPassword(),authorities);	
	}
}
