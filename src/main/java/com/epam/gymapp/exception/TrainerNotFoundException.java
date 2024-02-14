package com.epam.gymapp.exception;

public class TrainerNotFoundException extends RuntimeException {

  private static final String MESSAGE = "Trainer is not found";
  private static final String MESSAGE_FORMAT = "Trainer with ID %d is not found";

  public TrainerNotFoundException(Number id) {
    super(id == null ? MESSAGE : String.format(MESSAGE_FORMAT, id.intValue()));
  }
}
