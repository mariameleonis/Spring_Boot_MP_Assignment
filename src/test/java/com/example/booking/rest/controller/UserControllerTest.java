package com.example.booking.rest.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booking.entity.User;
import com.example.booking.exception.BusinessException;
import com.example.booking.exception.UserNotFoundException;
import com.example.booking.rest.controller.mapper.UserDtoMapper;
import com.example.booking.rest.model.UserRequest;
import com.example.booking.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  public static final String USER_NAME = "John";
  public static final String USER_EMAIL = "john@example.com";
  public static final Long USER_ID = 1L;

  public static final User USER = User.builder()
      .id(USER_ID)
      .name(USER_NAME)
      .email(USER_EMAIL)
      .build();
  private MockMvc mockMvc;

  @Mock
  private UserService service;

  @Mock
  private UserDtoMapper mapper;

  @InjectMocks
  private UserController controller;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void testGetById() throws Exception {
    when(service.findById(USER_ID)).thenReturn(USER);

    mockMvc.perform(get("/api/users/{id}", USER_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.is(USER_ID.intValue())))
        .andExpect(jsonPath("$.name", Matchers.is(USER_NAME)))
        .andExpect(jsonPath("$.email", Matchers.is(USER_EMAIL)));
  }

  @Test
  void testGetById_UserNotFound() throws Exception {
    when(service.findById(USER_ID)).thenThrow(new UserNotFoundException(USER_ID));

    mockMvc.perform(get("/api/users/{id}", USER_ID))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetByEmail() throws Exception {
    when(service.findByEmail(USER_EMAIL)).thenReturn(USER);

    mockMvc.perform(get("/api/users/email/{email}", USER_EMAIL))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.is(USER_ID.intValue())))
        .andExpect(jsonPath("$.name", Matchers.is(USER_NAME)))
        .andExpect(jsonPath("$.email", Matchers.is(USER_EMAIL)));
  }

  @Test
  void testCreate_whenSuccess() throws Exception {
    val userRequest = getUserRequest();

    when(mapper.map(userRequest)).thenReturn(USER);
    when(service.create(USER)).thenReturn(USER);

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", Matchers.is(1)))
        .andExpect(jsonPath("$.name", Matchers.is(USER_NAME)))
        .andExpect(jsonPath("$.email", Matchers.is(USER_EMAIL)));
  }

  private static UserRequest getUserRequest() {
    return UserRequest.builder()
        .name(USER_NAME)
        .email(USER_EMAIL)
        .build();
  }

  @Test
  void testCreate_whenFailed() throws Exception {
    val userRequest = getUserRequest();

    when(mapper.map(userRequest)).thenReturn(USER);
    when(service.create(USER)).thenThrow(BusinessException.class);

    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testUpdate_whenSuccess() throws Exception {
    val userRequest = getUserRequest();

    when(mapper.map(userRequest)).thenReturn(USER);
    when(service.update(USER)).thenReturn(USER);

    mockMvc.perform(put("/api/users/{id}", USER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.is(1)))
        .andExpect(jsonPath("$.name", Matchers.is(USER_NAME)))
        .andExpect(jsonPath("$.email", Matchers.is(USER_EMAIL)));
  }

  @Test
  void testUpdate_whenFailed() throws Exception {
    val userRequest = getUserRequest();

    when(mapper.map(userRequest)).thenReturn(USER);
    when(service.update(USER)).thenThrow(BusinessException.class);

    mockMvc.perform(put("/api/users/{id}", USER_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testDelete() throws Exception {
    Long userId = 1L;

    mockMvc.perform(delete("/api/users/{id}", userId))
        .andExpect(status().isNoContent());

   verify(service).delete(userId);
  }

  private String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}