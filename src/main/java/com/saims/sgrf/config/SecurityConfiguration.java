package com.saims.sgrf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.saims.sgrf.enums.Rol;
import com.saims.sgrf.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfiguration {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UsuarioService userService;
	
	@Bean
	SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
		http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests(request ->request.requestMatchers("/sgrf/login/**")
				.permitAll()
				.requestMatchers("/sgrf/imagenes/**").permitAll()
				.requestMatchers("/sgrf/admin/**").hasAnyAuthority(Rol.ADMIN.name())
				.requestMatchers("/sgrf/user/**").hasAnyAuthority(Rol.USER.name())
				.anyRequest().authenticated())
		.sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userService.userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
		
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
}
