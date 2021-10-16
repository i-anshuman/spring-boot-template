package com.application.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class CustomException extends RuntimeException {

  private static final long serialVersionUID = 31L;
  
  @Getter
  private HttpStatus status;
  
  public CustomException(String message) {
    super(message);
  }
  
  public CustomException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
