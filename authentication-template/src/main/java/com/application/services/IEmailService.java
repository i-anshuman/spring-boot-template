package com.application.services;

public interface IEmailService {
  boolean sendVerificationMail(String to, String username, String token);
  boolean sendPasswordResetMail(String to, String username, String token);
}
