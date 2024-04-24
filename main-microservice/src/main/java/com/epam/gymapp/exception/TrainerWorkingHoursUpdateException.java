package com.epam.gymapp.exception;

public class TrainerWorkingHoursUpdateException extends RuntimeException {

  private static final String MESSAGE = "Failed to update trainer's working hours. Please, try again after some time!";

  public TrainerWorkingHoursUpdateException() {
    super(MESSAGE);
  }
}
