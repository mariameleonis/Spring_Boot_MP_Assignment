package com.example.booking.rest.controller;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("/hello")
  public String saySecuredHello(JwtAuthenticationToken jwtAuthenticationToken){
    if (jwtAuthenticationToken == null) {
      return "Hello! You are not authenticated.";
    }
    return "Hello Authenticated User: "+jwtAuthenticationToken.getName();
  }
}
