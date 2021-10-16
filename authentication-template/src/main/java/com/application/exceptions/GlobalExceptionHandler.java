package com.application.exceptions;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.application.dto.responses.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Handle exception thrown when request method is not valid.
   */
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    
    StringBuilder message = new StringBuilder(ex.getMethod() + " method not allowed. ");
    message.append("Supported method(s) " + Arrays.toString(ex.getSupportedMethods()));

    return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(new ErrorResponse(message.toString(), LocalDateTime.now()));
  }

  /**
   * Handle exception thrown when a required path variable is missing.
   */
  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    
    String message = "Missing path variable " + ex.getVariableName();

    return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .body(new ErrorResponse(message, LocalDateTime.now()));
  }

  /**
   * Handle exception thrown when requested handler doesn't exist.
   */
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    
    String message = "Handler not found for " + ex.getHttpMethod()  + " " + ex.getRequestURL();
    
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(message, LocalDateTime.now()));
  }

  /**
   * Handle exception thrown when request body has error.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    
    StringBuilder message = new StringBuilder("Validation error. ");
    
    ex.getBindingResult()
      .getFieldErrors()
      .forEach(error -> message.append(error.getDefaultMessage()));
    
    return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(new ErrorResponse(message.toString(), LocalDateTime.now()));
  }
  
  /**
   * Handle exception thrown when request body is empty when it is not supposed to be.
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .body(new ErrorResponse("Empty request body.", LocalDateTime.now()));
  }

  /**
   * Handle AccessDeniedException thrown when request doesn't has the authority.
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(ex.getMessage(), LocalDateTime.now()));
  }
  
  /**
   * Handle BadCredentialsException thrown when authentication credential is incorrect.
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(ex.getMessage(), LocalDateTime.now()));
  }
  
  /**
   * Handle DisabledException thrown when account is not enabled.
   */
  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<?> handleDisabledException(DisabledException ex) {
    return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(ex.getMessage(), LocalDateTime.now()));
  }
  
  /**
   * Handle CustomException.
   */
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> handleCustomException(CustomException ex) {
    return ResponseEntity
            .status(ex.getStatus())
            .body(new ErrorResponse(ex.getMessage(), LocalDateTime.now()));
  }
}
