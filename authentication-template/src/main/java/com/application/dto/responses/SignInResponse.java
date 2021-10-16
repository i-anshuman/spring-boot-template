package com.application.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SignInResponse {
  private long id;
  private String username;
  private String email;
  private String token;
  private boolean activated;
  private List<String> authorities;
}
