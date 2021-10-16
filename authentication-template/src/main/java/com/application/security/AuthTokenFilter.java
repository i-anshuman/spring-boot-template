package com.application.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.application.services.UserDetailsServiceImpl;
import com.application.utils.JWTUtils;

public class AuthTokenFilter extends OncePerRequestFilter {

  @Autowired
  private JWTUtils jwtUtils;
  
  @Autowired
  private UserDetailsServiceImpl userDetailsService;
  
  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {    
    try {
      String token = getJWTTokenFromAuthHeader(request);
      
      if (token != null && jwtUtils.isValidToken(token)) {
        String username = jwtUtils.getSubject(token);
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    catch (Exception ex) {
      logger.error("Can't set user authentication: {}", ex);
    }
    
    filterChain.doFilter(request, response);
  }
  
  private String getJWTTokenFromAuthHeader(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    
    if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7, authHeader.length());
    }
    return null;
  }
}
