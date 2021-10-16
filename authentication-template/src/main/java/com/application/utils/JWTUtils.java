package com.application.utils;

import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.application.security.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTUtils {
  private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);
  
  @Value("${security.jwt.SECRET}")
  private String secret;
  
  @Value("${security.jwt.VALIDITY}")
  private int validity;
  
  @Value("${security.jwt.ISSUER}")
  private String issuer;
  
  public Claims getAllClaims (String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
  }
  
  public <T> T getClaim (String token, Function<Claims, T> resolver) {
    return resolver.apply(getAllClaims(token));
  }
  
  public String getSubject (String token) {
    return getClaim(token, Claims::getSubject);
  }
  
  public Date getExpirationTime (String token) {
    return getClaim(token, Claims::getExpiration);
  }
  
  public Date getIssueTime (String token) {
    return getClaim(token, Claims::getIssuedAt);
  }
  
  public String getIssuer (String token) {
    return getClaim(token, Claims::getIssuer);
  }
  
  public String generateToken (Authentication authentication) {
    UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
    
    return Jwts.builder()
               .setSubject(principal.getUsername())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + validity))
               .setIssuer(issuer)
               .signWith(SignatureAlgorithm.HS512, secret)
               .compact();
  }
  
  public boolean isValidToken (String token) {
    try {
      getAllClaims(token);
      return true;
    }
    catch (SignatureException ex) {
      logger.error("Invalid JWT signature: {} ", ex.getMessage());
    }
    catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token: {} ", ex.getMessage());
    }
    catch (ExpiredJwtException ex) {
      logger.error("JWT token is expired: {} ", ex.getMessage());
    }
    catch (UnsupportedJwtException ex) {
      logger.error("JWT token is unsupported: {} ", ex.getMessage());
    }
    catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty: {} ", ex.getMessage());
    }
    return false;
  }
}
