package com.application.dto.requests;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordResetRequest {
  
  @NotBlank(message = "{account.password.blank}")
  private String password;
  
  @NotBlank(message = "{account.password.blank}")
  private String rePassword;
}
