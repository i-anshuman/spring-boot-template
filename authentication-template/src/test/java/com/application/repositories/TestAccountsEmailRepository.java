package com.application.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.application.core.Mail;
import com.application.entities.AccountsEmail;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAccountsEmailRepository {
  
  @Autowired
  private AccountsEmailRepository accountsEmailRepository;
  
  private static String token;
  
  @BeforeAll
  public static void setUUID() {
    token = UUID.randomUUID().toString();
  }

  @BeforeEach
  public void setup() {
    AccountsEmail accountsEmail = AccountsEmail.builder()
                                    .email("anshuman@gmail.com")
                                    .token(token)
                                    .type(Mail.VERIFICATION)
                                    .build();
    accountsEmailRepository.save(accountsEmail);
    System.out.println(accountsEmail.getId() + " -> " + accountsEmail);
  }
  
  @Test
  @Order(1)
  public void testFindByTokenAndTypeThatExists() {
    assertTrue(accountsEmailRepository.findByTokenAndType(token, Mail.VERIFICATION).isPresent());
  }
  
  @Test
  @Order(2)
  public void testFindByTokenAndTypeThatDoesNotExist() {
    assertTrue(accountsEmailRepository.findByTokenAndType(UUID.randomUUID().toString(), Mail.RECOVERY).isEmpty());
  }
  
  @Test
  @Order(3)
  public void testFindByTokenAndTypeWhereTokenDoesNotExist() {
    assertTrue(accountsEmailRepository.findByTokenAndType(UUID.randomUUID().toString(), Mail.VERIFICATION).isEmpty());
  }
  
  @Test
  @Order(4)
  public void testFindByTokenAndTypeWhereTypeDoesNotExist() {
    assertTrue(accountsEmailRepository.findByTokenAndType(token, Mail.RECOVERY).isEmpty());
  }
}
