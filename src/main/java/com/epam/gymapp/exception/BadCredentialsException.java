package com.epam.gymapp.exception;

public class BadCredentialsException extends RuntimeException {

  private static final String MESSAGE = "Specified wrong username or password";

  public BadCredentialsException() {
    super(MESSAGE);
  }
}
