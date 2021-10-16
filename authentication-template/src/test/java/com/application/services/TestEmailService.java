package com.application.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestEmailService {

  @Autowired
  private IEmailService emailService;

  @Test
  void testSendVerificationMail() {
    assertTrue(emailService.sendVerificationMail("anshuman844@gmail.com", "Anshuman", UUID.randomUUID().toString()));
  }

  @Test
  void testSendPasswordResetMail() {
    assertTrue(emailService.sendPasswordResetMail("anshuman844@gmail.com", "Anshuman", UUID.randomUUID().toString()));
  }
}
