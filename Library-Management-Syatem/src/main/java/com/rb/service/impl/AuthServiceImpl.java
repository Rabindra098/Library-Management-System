package com.rb.service.impl;

import com.rb.config.JwtProvider;
import com.rb.domain.UserRole;
import com.rb.exception.UserException;
import com.rb.mapper.UserMapper;
import com.rb.model.PasswordResetToken;
import com.rb.model.User;
import com.rb.paylode.dto.UserDTO;
import com.rb.paylode.response.AuthResponse;
import com.rb.repository.PasswordResetTokenRepository;
import com.rb.repository.UserRepository;
import com.rb.service.AuthService;
import com.rb.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final EmailService emailService;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;
	
	private final UserRepository userRepository;
	
	private final CustomeUserServiceImpl customeUserServiceImpl;
	
	private final PasswordResetTokenRepository passwordResetTokenRepository;
   
	@Override
	public AuthResponse login(String username, String password) throws UserException {

	    Authentication authentication = authenticate(username, password);

	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    String token = jwtProvider.generateToken(authentication);

	    User user = userRepository.findByEmail(username);

	    user.setLastLogin(LocalDateTime.now());
	    userRepository.save(user);

	    AuthResponse response = new AuthResponse();
	    response.setTitle("Login success");
	    response.setMessage("Welcome Back " + username);
	    response.setJwt(token);
	    response.setUserDTO(UserMapper.toDTO(user));

	    return response;
	}
	
	private Authentication authenticate(String username, String password) throws UserException {

		UserDetails userDetails =customeUserServiceImpl.loadUserByUsername(username);
	    if (userDetails == null) {
	        throw new UserException("User not found with email - " + username);
	    }
	    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
	        throw new UserException("Password not match");
	    }
	    return new UsernamePasswordAuthenticationToken(
	            username,
	            null,
	            userDetails.getAuthorities()
	    );
	}

	@Override
	public AuthResponse signup(UserDTO req) throws UserException {
		User user = userRepository.findByEmail(req.getEmail());
		
		if(user != null) {
			throw new UserException("Email is already registerd");
		}
		User createdUser = new User();
		createdUser.setEmail(req.getEmail());
		createdUser.setPassword(passwordEncoder.encode(req.getPassword()));
		createdUser.setPhone(req.getPhone());
		createdUser.setFullName(req.getFullName());
		createdUser.setLastLogin(LocalDateTime.now());
		createdUser.setRole(UserRole.ROLE_USER);
		
		User savedUser=userRepository.save(createdUser);
		Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = jwtProvider.generateToken(auth);
		AuthResponse response = new AuthResponse();
		response.setJwt(jwt);
		response.setTitle("Welcome "+createdUser.getFullName());
		response.setMessage("Register success");
		response.setUserDTO(UserMapper.toDTO(savedUser));
		return response;
	}

	@Transactional
	public void createPasswordResetToken(String email) throws UserException {
		
		String frontendUrl="http://localhost:5173";
		
		User user=userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UserException("User not found with given email");
		}
		
		String token=UUID.randomUUID().toString();
		
		PasswordResetToken resetToken=PasswordResetToken.builder()
				.token(token)
				.user(user)
				.expiryDate(LocalDateTime.now().plusMinutes(5))
				.build();
		passwordResetTokenRepository.save(resetToken);
		String resetLink=frontendUrl+token;
		String subject="Password Reset Request";
		String body="You requested to reset your password. Use this link (valid 5 min)";
		
		emailService.sendEmail(user.getEmail(), subject, body);
	}

	@Transactional
	public void resetPassword(String token, String newPassword) throws UserException {
		PasswordResetToken resetToken=passwordResetTokenRepository.findByToken(token)
							.orElseThrow(()-> new UserException("Token Not Valid!"));
		if(resetToken.isExpired()) {
			passwordResetTokenRepository.delete(resetToken);
			throw new UserException("Token Expired");
		}
		User user = resetToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		passwordResetTokenRepository.delete(resetToken);
		
	}
	
}
