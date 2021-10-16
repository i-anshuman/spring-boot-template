package com.application.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.application.entities.User;
import com.application.repositories.UserRepository;
import com.application.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  
  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
                  .orElseThrow(() -> new UsernameNotFoundException("Username '" + username + "' doesn't exist."));
    return UserDetailsImpl.build(user);
  }

}
