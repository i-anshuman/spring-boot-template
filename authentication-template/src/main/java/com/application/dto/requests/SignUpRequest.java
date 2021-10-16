package com.application.dto.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(doNotUseGetters = true)
public class SignUpRequest {
  
  @NotBlank(message = "{account.username.blank}")
  private String username;
  
  @NotBlank(message = "{account.password.blank}")
  private String password;
  
  @NotBlank(message = "{signup.email.blank}")
  @Email(message = "{signup.email.invalid}")
  private String email;
}
