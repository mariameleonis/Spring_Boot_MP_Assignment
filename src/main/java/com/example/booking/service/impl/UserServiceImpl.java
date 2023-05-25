package com.example.booking.service.impl;

import com.example.booking.entity.User;
import com.example.booking.exception.BusinessException;
import com.example.booking.exception.UserNotFoundException;
import com.example.booking.repository.UserRepository;
import com.example.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  public static final String EMAIL_ADDRESS_ALREADY_EXISTS = "Email address already exists";

  private final UserRepository userRepository;

  @Override
  public User findById(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }

  @Override
  public User create(User user) {
    return saveOrUpdate(user);
  }

  @Override
  public User update(User user) {
    val existingUser = userRepository.findById(user.getId())
        .orElseThrow(() -> new UserNotFoundException(user.getId()));
    return saveOrUpdate(map(user, existingUser));
  }

  @Override
  public void delete(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(email));
  }

  private User saveOrUpdate(User user) throws BusinessException {
    try {
      return userRepository.save(user);
    } catch (DataIntegrityViolationException e) {
      throw new BusinessException(
          String.format("%s (%s)", EMAIL_ADDRESS_ALREADY_EXISTS, user.getEmail()));
    }
  }

  private User map(User source, User target) {
    target.setName(source.getName());
    target.setEmail(source.getEmail());
    return target;
  }
}
