package com.application.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.application.entities.User;

public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 5L;
  private Long id;
  private String username;
  private String password;
  private String email;
  private boolean activated;
  private Collection<? extends GrantedAuthority> authorities;
  
  public UserDetailsImpl(Long id, String username, String password, String email, boolean activated,
      Collection<? extends GrantedAuthority> authorities) {
    super();
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.activated = activated;
    this.authorities = authorities;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }
  
  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.activated;
  }
  
  public static UserDetailsImpl build (User user) {
    List<GrantedAuthority> authorities = user.getRoles().stream()
                                             .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                                             .collect(Collectors.toList());
    return new UserDetailsImpl(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getEmail(),
        user.isActivated(),
        authorities
    );
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    return ((UserDetailsImpl) obj).getId().equals(this.id);
  }
}
