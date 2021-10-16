package com.application.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.dto.requests.AccountResetRequest;
import com.application.dto.requests.PasswordResetRequest;
import com.application.dto.requests.SignInRequest;
import com.application.dto.requests.SignUpRequest;
import com.application.dto.responses.MessageResponse;
import com.application.dto.responses.SignInResponse;
import com.application.entities.User;
import com.application.security.UserDetailsImpl;
import com.application.services.IAccountService;
import com.application.utils.JWTUtils;

@RestController
@RequestMapping(path = "/account")
@CrossOrigin(origins = { "${security.ORIGINS}" })
public class AccountController {
  
  @Autowired
  private AuthenticationManager authManager;
  
  @Autowired
  private IAccountService accountService;

  @Autowired
  private JWTUtils jwtUtils;

  @PostMapping("/signup")
  public ResponseEntity<?> signup (@RequestBody @Valid SignUpRequest signup) {
    User user = accountService.save(signup);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }
  
  @PostMapping("/signin")
  public ResponseEntity<?> signin (@RequestBody @Valid SignInRequest signin) {
    Authentication authentication = authManager.authenticate(
        new UsernamePasswordAuthenticationToken(signin.getUsername(), signin.getPassword()));
    
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtUtils.generateToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> authorities = userDetails.getAuthorities().stream()
                                .map(role -> role.getAuthority())
                                .collect(Collectors.toList());

    SignInResponse response = SignInResponse.builder()
                                .id(userDetails.getId())
                                .username(userDetails.getUsername())
                                .email(userDetails.getEmail())
                                .activated(userDetails.isEnabled())
                                .token(token)
                                .authorities(authorities)
                                .build();
    
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }
  
  @PostMapping("/password-reset-link")
  public ResponseEntity<?> sendPasswordResetLink (@RequestBody @Valid AccountResetRequest accountReset) {
    accountService.sendPasswordResetMail(accountReset);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MessageResponse("Password reset link has been sent to your registered email.", LocalDateTime.now()));
  }
  
  @PostMapping("/verification-link")
  public ResponseEntity<?> sendVerificationLink (@RequestBody @Valid AccountResetRequest accountReset) {
    accountService.sendVerificationMail(accountReset);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MessageResponse("Account verification link has been sent to your email.", LocalDateTime.now()));
  }
  
  @GetMapping("/verify/{token}")
  public ResponseEntity<?> verifyAndActivateAccount (@PathVariable String token) {
    accountService.verifyAndActivate(token);
    return ResponseEntity.status(HttpStatus.OK)
            .body(new MessageResponse("Account activated successfully.", LocalDateTime.now()));
  }
  
  @PostMapping("/reset-password/{token}")
  public ResponseEntity<?> verifyAndResetPassword (@PathVariable String token, @RequestBody @Valid PasswordResetRequest passwordReset) {
    accountService.verifyAndResetPassword(token, passwordReset);
    return ResponseEntity.status(HttpStatus.OK)
            .body(new MessageResponse("Password changed successfully.", LocalDateTime.now())); 
  }
}
