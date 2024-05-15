package com.epam.gymapp.mainmicroservice.exception;

public class UserNotFoundException extends RuntimeException {

  private static final String MESSAGE = "User is not found";
  private static final String MESSAGE_FORMAT = "User with username '%s' is not found";

  public UserNotFoundException(String username) {
    super(username == null || username.isBlank() ? MESSAGE
        : String.format(MESSAGE_FORMAT, username));
  }
}
