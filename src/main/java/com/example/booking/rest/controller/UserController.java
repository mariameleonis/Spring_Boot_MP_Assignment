package com.example.booking.rest.controller;

import com.example.booking.entity.User;
import com.example.booking.rest.controller.mapper.UserDtoMapper;
import com.example.booking.rest.model.UserRequest;
import com.example.booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;
  private final UserDtoMapper mapper;

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<User> getByEmail(@PathVariable String email) {
    return ResponseEntity.ok(service.findByEmail(email));
  }

  @PostMapping
  public ResponseEntity<User> create(@RequestBody UserRequest userRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(mapper.map(userRequest)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserRequest userRequest) {
    User user = mapper.map(userRequest);
    user.setId(id);
    return ResponseEntity.ok(service.update(user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
