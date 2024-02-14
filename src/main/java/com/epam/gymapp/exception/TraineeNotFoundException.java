package com.epam.gymapp.exception;

public class TraineeNotFoundException extends RuntimeException {

  private static final String MESSAGE = "Trainee is not found";
  private static final String MESSAGE_FORMAT = "Trainee with ID %d is not found";

  public TraineeNotFoundException(Number id) {
    super(id == null ? MESSAGE : String.format(MESSAGE_FORMAT, id.intValue()));
  }
}
