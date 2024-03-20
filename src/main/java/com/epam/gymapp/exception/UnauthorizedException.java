package com.epam.gymapp.exception;

public class UnauthorizedException extends RuntimeException {

  private static final String MESSAGE = "Unauthorized access";

  public UnauthorizedException() {
    super(MESSAGE);
  }
}
