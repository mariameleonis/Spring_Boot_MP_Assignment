package com.example.booking.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.booking.entity.User;
import com.example.booking.exception.BusinessException;
import com.example.booking.exception.UserNotFoundException;
import com.example.booking.repository.UserRepository;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  public static final String EMAIL = "test@example.com";
  public static final long USER_ID = 123L;
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void getUserByEmail() throws BusinessException {
    val user = new User();

    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

    val result = userService.findByEmail(EMAIL);

    assertEquals(user, result);
    verify(userRepository, times(1)).findByEmail(EMAIL);
  }

  @Test
  void testGetUserByEmail_NotFound() {
    when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

    assertThrows(BusinessException.class, () -> userService.findByEmail(EMAIL));
    verify(userRepository, times(1)).findByEmail(EMAIL);
  }

  @Test
  void getUserById() throws BusinessException {
    long id = 123;
    val user = new User();

    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    val result = userService.findById(id);

    assertEquals(user, result);
    verify(userRepository, times(1)).findById(id);
  }

  @Test
  void testGetUserById_NotFound() {
    long id = 123;

    when(userRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(BusinessException.class, () -> userService.findById(id));
    verify(userRepository, times(1)).findById(id);
  }

  @Test
  void create() throws BusinessException {
    val user = new User();

    when(userRepository.save(user)).thenReturn(user);

    val result = userService.create(user);

    assertEquals(user, result);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void update() throws BusinessException {
    val user = User.builder().id(USER_ID).build();

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    val result = userService.update(user);

    assertEquals(user, result);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void testUpdate_NotFound() {
    val user = User.builder().id(USER_ID).build();

    when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.update(user));
  }

  @Test
  void testDeleteById() {
    userService.delete(USER_ID);

    verify(userRepository, times(1)).deleteById(USER_ID);
  }
}