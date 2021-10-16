package com.application.services;

import static com.application.core.Role.USER;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.core.Mail;
import com.application.dto.requests.AccountResetRequest;
import com.application.dto.requests.PasswordResetRequest;
import com.application.dto.requests.SignUpRequest;
import com.application.entities.AccountsEmail;
import com.application.entities.Role;
import com.application.entities.User;
import com.application.exceptions.CustomException;
import com.application.repositories.RoleRepository;
import com.application.repositories.UserRepository;

@Service
@Transactional
public class AccountServiceImpl implements IAccountService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder encoder;
  
  @Autowired
  private IAccountsEmailService accountsEmailService;
  
  @Autowired
  private IEmailService emailService;
  
  //@Autowired
  //private UserEntityMapper userMapper;

  @Override
  public User save(SignUpRequest user) {
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new CustomException("Email is already in use.", HttpStatus.CONFLICT);
    }
    
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new CustomException("Username is already taken.", HttpStatus.CONFLICT);
    }
    
    Role role = roleRepository.findByRole(USER)
                  .orElseThrow(() -> new CustomException("Role not found.", HttpStatus.FAILED_DEPENDENCY));
    
    Set<Role> roles = new HashSet<>();
    roles.add(role);
    
    User newUser = User.builder()
                       .username(user.getUsername())
                       .email(user.getEmail())
                       .password(encoder.encode(user.getPassword()))
                       .roles(roles)
                       .build();
    
    userRepository.save(newUser);
    
    AccountsEmail accountsEmail = accountsEmailService.create(user.getEmail(), Mail.VERIFICATION);
    emailService.sendVerificationMail(user.getEmail(), user.getUsername(), accountsEmail.getToken());
    
    return newUser;
  }

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public void sendPasswordResetMail(AccountResetRequest accountReset) {
    User user = userRepository.findByEmail(accountReset.getEmail())
                  .orElseThrow(() -> new CustomException("Email not found.", HttpStatus.NOT_FOUND));
    accountsEmailService.deleteAllPreviousByEmailAndType(user.getEmail(), Mail.RECOVERY);
    AccountsEmail accountsEmail = accountsEmailService.create(user.getEmail(), Mail.RECOVERY);
    if (!emailService.sendPasswordResetMail(user.getEmail(), user.getUsername(), accountsEmail.getToken())) {
      throw new CustomException("Failed to send password reset link.", HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public void sendVerificationMail(AccountResetRequest accountReset) {
    User user = userRepository.findByEmail(accountReset.getEmail())
                  .orElseThrow(() -> new CustomException("Email not found.", HttpStatus.NOT_FOUND));

    if (user.isActivated()) {
      throw new CustomException("Account is already verified.", HttpStatus.ALREADY_REPORTED);
    }
    accountsEmailService.deleteAllPreviousByEmailAndType(user.getEmail(), Mail.VERIFICATION);
    AccountsEmail accountsEmail = accountsEmailService.create(user.getEmail(), Mail.VERIFICATION);
    emailService.sendVerificationMail(user.getEmail(), user.getUsername(), accountsEmail.getToken());
  }

  @Override
  public void verifyAndActivate(String token) {
    AccountsEmail accountsEmail = accountsEmailService.isValidToken(token, Mail.VERIFICATION);
    User user = userRepository.findByEmail(accountsEmail.getEmail())
                  .orElseThrow(() -> new CustomException("Email not found.", HttpStatus.NOT_FOUND));
    user.setActivated(true);
    userRepository.save(user);
  }

  @Override
  public void verifyAndResetPassword(String token, PasswordResetRequest passwordResetRequest) {
    if (!passwordResetRequest.getPassword().equals(passwordResetRequest.getRePassword())) {
      throw new CustomException("Passwords do not match.", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    AccountsEmail accountsEmail = accountsEmailService.isValidToken(token, Mail.RECOVERY);
    
    User user = userRepository.findByEmail(accountsEmail.getEmail())
                  .orElseThrow(() -> new CustomException("Email not found.", HttpStatus.NOT_FOUND));
    
    //passwordResetRequest.setPassword(encoder.encode(passwordResetRequest.getPassword()));
    //userMapper.updatePasswordFromDTO(passwordResetRequest, user);
    user.setPassword(encoder.encode(passwordResetRequest.getPassword()));
    userRepository.save(user);
  }
}
