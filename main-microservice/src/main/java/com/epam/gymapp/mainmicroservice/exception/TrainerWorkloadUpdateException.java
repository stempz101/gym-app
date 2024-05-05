package com.epam.gymapp.mainmicroservice.exception;

public class TrainerWorkloadUpdateException extends RuntimeException {

  private static final String MESSAGE = "Failed to update trainers' workload. Please, try again after some time!";

  public TrainerWorkloadUpdateException() {
    super(MESSAGE);
  }
}
