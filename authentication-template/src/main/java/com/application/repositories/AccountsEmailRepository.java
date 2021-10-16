package com.application.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.core.Mail;
import com.application.entities.AccountsEmail;

@Repository
public interface AccountsEmailRepository extends JpaRepository<AccountsEmail, Long> {
  Optional<AccountsEmail> findByTokenAndType(String token, Mail type);
  void deleteAllByEmailAndType(String token, Mail type);
}
