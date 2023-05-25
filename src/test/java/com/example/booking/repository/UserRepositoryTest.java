package com.example.booking.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.booking.entity.User;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.liquibase.enabled=false",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void findByEmail() {

    val email = "liv_delano@example.com";
    val user = User.builder()
        .name("Liv Delano")
        .email(email)
        .build();

    userRepository.save(user);

    val resultUser = userRepository.findByEmail(email);

    assertTrue(resultUser.isPresent());
    assertEquals(user.getName(), resultUser.get().getName());
    assertEquals(email, resultUser.get().getEmail());
  }
}