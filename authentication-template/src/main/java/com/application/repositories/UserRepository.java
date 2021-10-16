package com.application.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Boolean existsByEmail(String email);
  Boolean existsByUsername(String username);
  Optional<User> findByEmail(String email);
  Optional<User> findByUsername(String username);
}
