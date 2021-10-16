package com.application.services;

import com.application.core.Mail;
import com.application.entities.AccountsEmail;

public interface IAccountsEmailService {
  AccountsEmail create(String email, Mail type);
  AccountsEmail isValidToken(String email, Mail type);
  void deleteAllPreviousByEmailAndType(String email, Mail type);
}
