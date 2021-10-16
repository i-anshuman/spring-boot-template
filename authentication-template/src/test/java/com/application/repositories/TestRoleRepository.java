package com.application.repositories;

import static com.application.core.Role.MODERATOR;
import static com.application.core.Role.USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.application.exceptions.CustomException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestRoleRepository {
  
  @Autowired
  private RoleRepository roleRepo;
  
  @BeforeEach
  public void setUp () {
    Role role = new Role(USER);
    roleRepo.save(role);
    assertNotNull(role.getId());
  }
  
  @Test
  @Order(1)
  public void testFindByRoleThatExists () throws Exception {
    Role role = roleRepo.findByRole(USER).orElseThrow(() -> new CustomException("Role not found."));
    assertNotNull(role);
  }
  
  @Test
  @Order(2)
  public void testFindByRoleThatDoesNotExist () throws Exception {
    assertThrows(CustomException.class, () -> roleRepo.findByRole(MODERATOR).orElseThrow(() -> new CustomException("Role not found.")));
  }
}
