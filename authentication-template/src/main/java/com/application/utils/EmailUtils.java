package com.application.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {
  
  @Autowired
  private JavaMailSender mailSender;
    
  public void sendEMail(String to, String subject, String body) throws MessagingException {
    MimeMessage helper = mailSender.createMimeMessage();
    MimeMessageHelper template = new MimeMessageHelper(helper, true);
    template.setTo(to);
    template.setSubject(subject);
    template.setText(body, true);
    mailSender.send(helper);
  }
}
