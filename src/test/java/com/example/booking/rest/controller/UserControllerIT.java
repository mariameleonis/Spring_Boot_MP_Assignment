package com.example.booking.rest.controller;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.example.booking.entity.User;
import com.example.booking.repository.UserRepository;
import com.example.booking.rest.model.UserRequest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql("test-data.sql")
class UserControllerIT {

  public static final long VALID_ID = 25L;
  public static final String API_USERS_EMAIL = "/api/users/email/%s";
  public static final long INVALID_ID = 38L;
  public static final String API_USERS_ID = "/api/users/%s";
  public static final String API_USERS = "/api/users";
  @Autowired
  private TestRestTemplate restTemplate;

  @SpyBean
  private UserRepository repository;

  @Test
  void testCreateUser_whenSuccess() {
    val name = "John Smith";
    val  email = "john_smith@example.com";

    val userRequest = createNewUserRequest(name, email);

    val responseEntity = this.restTemplate
        .postForEntity(API_USERS, userRequest, User.class);

    assertEquals(HttpStatusCode.valueOf(201), responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(name, responseEntity.getBody().getName());
    assertEquals(email, responseEntity.getBody().getEmail());

    val userCaptor = ArgumentCaptor.forClass(User.class);

    verify(repository).save(userCaptor.capture());

    val savedUser = userCaptor.getValue();
    assertEquals(name, savedUser.getName());
    assertEquals(email, savedUser.getEmail());
  }

  @Test
  void testCreateUser_whenEmailAlreadyExist() {
    val name = "John Smith";
    val email = "jane.smith@example.com";

    val userRequest = createNewUserRequest(name, email);

    val responseEntity = this.restTemplate
        .postForEntity(API_USERS, userRequest, User.class);

    assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());

    val userCaptor = ArgumentCaptor.forClass(User.class);

    verify(repository).save(userCaptor.capture());

    val triedToSaveUser = userCaptor.getValue();
    assertEquals(name, triedToSaveUser.getName());
    assertEquals(email, triedToSaveUser.getEmail());
  }

  @Test
  void testUpdateUser_whenSuccess() {
    val name = "NewName";
    val email = "new_email@example.com";

    val userRequest = createNewUserRequest(name, email);

    val responseEntity = this.restTemplate
        .exchange(format(API_USERS_ID, VALID_ID), HttpMethod.PUT, new HttpEntity<>(userRequest, new HttpHeaders()), User.class);

    assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(25, responseEntity.getBody().getId());
    assertEquals(name, responseEntity.getBody().getName());
    assertEquals(email, responseEntity.getBody().getEmail());

    val userCaptor = ArgumentCaptor.forClass(User.class);

    verify(repository).save(userCaptor.capture());

    val updatedUser = userCaptor.getValue();
    assertEquals(name, updatedUser.getName());
    assertEquals(email, updatedUser.getEmail());
  }

  @Test
  void testUpdateUser_whenEmailAlreadyExists() {
    val name = "John Smith";
    val email = "jane.smith@example.com";
    val userRequest = createNewUserRequest(name, email);

    val responseEntity = this.restTemplate
        .exchange(format(API_USERS_ID, VALID_ID), HttpMethod.PUT, new HttpEntity<>(userRequest, new HttpHeaders()), User.class);

    assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());

    val userCaptor = ArgumentCaptor.forClass(User.class);

    verify(repository).save(userCaptor.capture());

    val triedToUpdateUser = userCaptor.getValue();
    assertEquals(name, triedToUpdateUser.getName());
    assertEquals(email, triedToUpdateUser.getEmail());
  }

  @Test
  void testGetUserById_whenSuccess() {
    val responseEntity = this.restTemplate
        .exchange(format(API_USERS_ID, VALID_ID), HttpMethod.GET, null, User.class);

    assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(VALID_ID, responseEntity.getBody().getId());
    assertEquals("John Doe", responseEntity.getBody().getName());
    assertEquals("john.doe@example.com", responseEntity.getBody().getEmail());

    verify(repository).findById(VALID_ID);
  }

  @Test
  void testGetUserById_whenInvalidUserId() {
    val responseEntity = this.restTemplate
        .exchange(format(API_USERS_ID, "abc"), HttpMethod.GET, null, User.class);

    assertEquals(HttpStatus.BAD_REQUEST.value(), responseEntity.getStatusCode().value());

    verifyNoInteractions(repository);
  }

  @Test
  void testGetUserById_whenNotFound() {
    val responseEntity = this.restTemplate
        .exchange(format(API_USERS_ID, INVALID_ID), HttpMethod.GET, null, User.class);

    assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());

    verify(repository).findById(INVALID_ID);
  }

  @Test
  void testGetUserByEmail_whenSuccess() {
    val email = "john.doe@example.com";

    val responseEntity = this.restTemplate
        .exchange(format(API_USERS_EMAIL, email), HttpMethod.GET, null, User.class);

    assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(VALID_ID, responseEntity.getBody().getId());
    assertEquals("John Doe", responseEntity.getBody().getName());

    assertEquals(email, responseEntity.getBody().getEmail());

    verify(repository).findByEmail(email);
  }

  @Test
  void testGetUserByEmail_whenNotFound() {
    val email = "unknown@example.com";

    val responseEntity = this.restTemplate
        .exchange(format(API_USERS_EMAIL, email), HttpMethod.GET, null, User.class);

    assertEquals(HttpStatus.NOT_FOUND.value(), responseEntity.getStatusCode().value());

    verify(repository).findByEmail(email);
  }

  @Test
  void testDelete() {
    val response = restTemplate.exchange(format(API_USERS_ID, VALID_ID), HttpMethod.DELETE, null, Void.class);

    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());

    verify(repository).deleteById(VALID_ID);
  }

  private UserRequest createNewUserRequest(String name, String email) {
    return UserRequest.builder()
        .name(name)
        .email(email)
        .build();
  }

}