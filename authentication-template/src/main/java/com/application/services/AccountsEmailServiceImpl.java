package com.application.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.core.Mail;
import com.application.entities.AccountsEmail;
import com.application.exceptions.CustomException;
import com.application.repositories.AccountsEmailRepository;

@Service
@Transactional
public class AccountsEmailServiceImpl implements IAccountsEmailService {

  @Autowired
  private AccountsEmailRepository accountsEmailRepository;
  
  @Value("${app.mail.VALIDITY}")
  private int validity;

  @Override
  public AccountsEmail create(String email, Mail type) {
    AccountsEmail accountsEmail = AccountsEmail.builder()
                                    .email(email)
                                    .token(UUID.randomUUID().toString())
                                    .type(type)
                                    .build();
    return accountsEmailRepository.save(accountsEmail);
  }
  
  @Override
  public AccountsEmail isValidToken(String email, Mail type) {
    AccountsEmail accountsEmail = accountsEmailRepository.findByTokenAndType(email, type)
                                    .orElseThrow(() -> new CustomException("Token is not valid.", HttpStatus.NOT_ACCEPTABLE));

    long ageOfToken = Duration.between(accountsEmail.getSentTimestamp(), LocalDateTime.now()).toSeconds();
    if (ageOfToken >= validity) {
      throw new CustomException("Token is expired.", HttpStatus.NOT_ACCEPTABLE);
    }
    
    accountsEmailRepository.delete(accountsEmail);
    return accountsEmail;
  }

  @Override
  public void deleteAllPreviousByEmailAndType(String email, Mail type) {
    accountsEmailRepository.deleteAllByEmailAndType(email, type);
  }

}
