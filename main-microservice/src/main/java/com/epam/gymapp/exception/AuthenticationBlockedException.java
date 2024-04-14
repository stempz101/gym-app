package com.epam.gymapp.exception;

public class AuthenticationBlockedException extends RuntimeException {

  private static final String MESSAGE = "User is blocked from logging in due to too many failed attempts";

  public AuthenticationBlockedException() {
    super(MESSAGE);
  }
}
