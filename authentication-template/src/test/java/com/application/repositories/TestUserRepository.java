package com.application.repositories;

import static com.application.core.Role.USER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.application.entities.Role;
import com.application.entities.User;
import com.application.exceptions.CustomException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUserRepository {
  
  @Autowired
  private UserRepository userRepo;
  
  @Autowired
  private RoleRepository roleRepo;
  
  @BeforeEach
  public void setUp () throws Exception {
    Role userRole = new Role(USER);
    roleRepo.save(userRole);
    
    User user = new User();
    user.setUsername("anshuman");
    user.setPassword("029511");
    user.setEmail("anshuman@gmail.com");
    Set<Role> role = new HashSet<>();
    role.add(roleRepo.findByRole(USER).orElseThrow(() -> new CustomException("Role not found.")));
    user.setRoles(role);
    userRepo.save(user);
  }
  
  @Test
  @Order(1)
  public void testExistsByEmail () {
    assertTrue(userRepo.existsByEmail("anshuman@gmail.com"));
    assertFalse(userRepo.existsByEmail("anshuman@outlook.com"));
  }
  
  @Test
  @Order(2)
  public void testExistsByUsername () {
    assertTrue(userRepo.existsByUsername("anshuman"));
    assertFalse(userRepo.existsByUsername("anshuman_"));
  }
}
