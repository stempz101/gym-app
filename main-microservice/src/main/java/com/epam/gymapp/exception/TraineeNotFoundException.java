package com.epam.gymapp.exception;

public class TraineeNotFoundException extends RuntimeException {

  private static final String MESSAGE = "Trainee is not found";
  private static final String MESSAGE_FORMAT = "Trainee with username '%s' is not found";

  public TraineeNotFoundException(String username) {
    super(username == null || username.isBlank() ? MESSAGE
        : String.format(MESSAGE_FORMAT, username));
  }
}
