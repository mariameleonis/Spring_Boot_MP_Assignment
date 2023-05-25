package com.example.booking.exception;

public class UserNotFoundException extends NotFoundException {

  public static final String USER_NOT_FOUND_BY_ID_MESSAGE = "There is no user with ID %d.";

  public static final String USER_NOT_FOUND_BY_EMAIL_MESSAGE = "There is no user with email %s.";

  public UserNotFoundException(long userId) {
    super((String.format(USER_NOT_FOUND_BY_ID_MESSAGE, userId)));
  }

  public UserNotFoundException(String email) {
    super((String.format(USER_NOT_FOUND_BY_EMAIL_MESSAGE, email)));
  }
}
