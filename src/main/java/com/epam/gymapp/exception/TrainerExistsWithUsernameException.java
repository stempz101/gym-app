package com.epam.gymapp.exception;

public class TrainerExistsWithUsernameException extends RuntimeException {

  public static final String MESSAGE_FORMAT = "Trainer with username '%s' already exists";

  public TrainerExistsWithUsernameException(String username) {
    super(String.format(MESSAGE_FORMAT, username));
  }
}
