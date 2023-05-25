package com.example.booking.rest.controller.mapper;

import com.example.booking.entity.User;
import com.example.booking.rest.model.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

  User map(UserRequest source);

}
