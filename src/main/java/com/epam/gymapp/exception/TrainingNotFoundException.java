package com.epam.gymapp.exception;

public class TrainingNotFoundException extends RuntimeException {

  private static final String MESSAGE = "Training is not found";
  private static final String MESSAGE_FORMAT = "Training with ID %d is not found";

  public TrainingNotFoundException(Number id) {
    super(id == null ? MESSAGE : String.format(MESSAGE_FORMAT, id.intValue()));
  }
}
