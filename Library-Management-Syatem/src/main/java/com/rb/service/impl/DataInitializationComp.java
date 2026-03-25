package com.rb.service.impl;

import com.rb.domain.UserRole;
import com.rb.model.User;
import com.rb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComp implements CommandLineRunner {
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		initializeAdminUser();
	}
	
	private void initializeAdminUser() {
		String adminEmail="101rabindrakumar@gmail.com";
		String adminPassword="7327821430";
		
		if(userRepository.findByEmail(adminEmail)==null) {
			User user=User.builder()
					.password(passwordEncoder.encode(adminPassword))
					.email(adminEmail)
					.fullName("Rabindra")
					.role(UserRole.ROLE_ADMIN)
					.build();
			User admin=userRepository.save(user);
		}
	}

	
}
