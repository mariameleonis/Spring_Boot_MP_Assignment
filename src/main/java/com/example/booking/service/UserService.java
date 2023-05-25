package com.example.booking.service;

import com.example.booking.entity.User;
import com.example.booking.rest.model.UserRequest;

public interface UserService {

  User findById(Long userId);

  User create(User user);

  User update(User user);

  void delete(Long id);

  User findByEmail(String email);
}
