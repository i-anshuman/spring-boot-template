package com.application.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.application.utils.EmailUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class EmailServiceImpl implements IEmailService {
  
  private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Autowired
  private EmailUtils email;
  
  @Autowired
  private Configuration config;
  
  @Value("${security.ORIGINS}")
  private String origin;
  
  @Value("${app.mail.origin.VERIFICATION}")
  private String verificationURI;
  
  @Value("${app.mail.origin.RECOVER}")
  private String recoveryURI;
  
  @Value("${app.mail.SENDER}")
  private String sender;
  
  @Value("${app.mail.APPLICATION}")
  private String application;
  
  @Override
  public boolean sendVerificationMail(String to, String username, String token) {
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("username", username);
      model.put("sender", sender);
      model.put("link", origin + verificationURI + token);
      model.put("application", application);
      
      Template template = config.getTemplate("verification.ftl");
      String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
      
      email.sendEMail(to, "Account Verification", body);
      return true;
    }
    catch (IOException | TemplateException | MessagingException ex) {
      logger.error("Failed to send verification mail: {} ", ex.getMessage());
    }
    return false;
  }

  @Override
  public boolean sendPasswordResetMail(String to, String username, String token) {
    try {
      Map<String, Object> model = new HashMap<>();
      model.put("username", username);
      model.put("sender", sender);
      model.put("link", origin + recoveryURI + token);
      
      Template template = config.getTemplate("password-reset.ftl");
      String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
      
      email.sendEMail(to, "Password Reset", body);
      return true;
    }
    catch (IOException | TemplateException | MessagingException ex) {
      logger.error("Failed to send password reset link: {} ", ex.getMessage());
    }
    return false;
  }
}
