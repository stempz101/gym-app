package com.epam.gymapp.exception;

public class TraineeExistsWithUsernameException extends RuntimeException {

  public static final String MESSAGE_FORMAT = "Trainee with username '%s' already exists";

  public TraineeExistsWithUsernameException(String username) {
    super(String.format(MESSAGE_FORMAT, username));
  }
}
