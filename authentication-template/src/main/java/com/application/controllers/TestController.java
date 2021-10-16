package com.application.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
@CrossOrigin(origins = { "${security.ORIGINS}" })
public class TestController {
  
  @GetMapping("/all")
  public String helloToAll () {
    return "Hello!! May I know your identity ??";
  }
  
  @GetMapping("/user")
  @PreAuthorize("hasRole('USER')")
  public String helloUser () {
    return "Hello User !!";
  }
  
  @GetMapping("/moderator")
  @PreAuthorize("hasRole('MODERATOR')")
  public String helloModerator () {
    return "Hello Moderator !!";
  }
  
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String helloAdmin () {
    return "Hello Sir !!";
  }
}
