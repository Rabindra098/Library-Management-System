package com.rb.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private JwtValidator jwtValidator;
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		return http
				.sessionManagement(management->management
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						.authorizeHttpRequests(Authorrize -> Authorrize
								.requestMatchers("/api/subscription-plan/admin/**").hasRole("ADMIN")
								.requestMatchers("/api/admin/**").hasRole("ADMIN")
								.requestMatchers("/api/**").authenticated()
								.anyRequest().permitAll()
				)
				.addFilterBefore((Filter) jwtValidator, BasicAuthenticationFilter.class)
				.csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.build();
	}
	private CorsConfigurationSource corsConfigurationSource() {
	    return new CorsConfigurationSource() {
	        @Override
	        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	            CorsConfiguration cfg = new CorsConfiguration();
	            cfg.setAllowCredentials(true);
	            cfg.setAllowedOrigins(
	                    Arrays.asList(
	                            "http://localhost:5173/",
	                            "https://zoshlibrary.com"
	                    )
	            );
	            cfg.setAllowedMethods(Collections.singletonList("*"));
	            cfg.setAllowedHeaders(Collections.singletonList("*"));
	            cfg.setExposedHeaders(Collections.singletonList("Authorization"));
	            cfg.setMaxAge(360L);
	            return cfg;
	        }
	    };
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
