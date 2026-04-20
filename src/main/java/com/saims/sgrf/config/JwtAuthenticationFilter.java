package com.saims.sgrf.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saims.sgrf.service.UsuarioService;
import com.saims.sgrf.service.jwtService;

//import io.micrometer.common.util.StringUtils;
import org.apache.commons.lang3.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    //private final String SECRET = System.getenv("j@mes"); // Cambiado para usar una variable de entorno
	private final jwtService jwtService;
	private final UsuarioService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, java.io.IOException {
		final String authHeader = request.getHeader(this.HEADER);
		final String jwt;
		final String username;
		if(existeToken(request,response)) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		username = jwtService.extractUserName(jwt);
		System.out.print(username);
		if(StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
			if(jwtService.isTokenValid(jwt, userDetails)) {
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				securityContext.setAuthentication(token);
				SecurityContextHolder.setContext(securityContext);
			}
		}
		filterChain.doFilter(request, response);
	}
	public boolean existeToken(HttpServletRequest request, HttpServletResponse response) {
		String authHeader = request.getHeader(HEADER);
		if(StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, PREFIX)){
			return true;
		}
		else 
			return false;
	}

}
