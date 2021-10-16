package com.application.services;

import java.util.Optional;

import com.application.dto.requests.AccountResetRequest;
import com.application.dto.requests.PasswordResetRequest;
import com.application.dto.requests.SignUpRequest;
import com.application.entities.User;

public interface IAccountService {
  public User save(SignUpRequest user);
  public boolean existsByEmail(String email);
  public boolean existsByUsername(String username);
  public Optional<User> findByEmail(String email);
  public Optional<User> findByUsername(String username);
  public void sendVerificationMail(AccountResetRequest accountReset);
  public void sendPasswordResetMail(AccountResetRequest accountReset);
  public void verifyAndActivate(String token);
  public void verifyAndResetPassword(String token, PasswordResetRequest passwordResetRequest);
}
