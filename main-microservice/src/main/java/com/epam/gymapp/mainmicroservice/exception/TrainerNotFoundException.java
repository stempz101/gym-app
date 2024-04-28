package com.epam.gymapp.mainmicroservice.exception;

import java.util.List;

public class TrainerNotFoundException extends RuntimeException {

  private static final String MESSAGE = "Trainer is not found";
  private static final String MESSAGE_FORMAT_USERNAME = "Trainer with username '%s' is not found";
  private static final String MESSAGE_FORMAT_USERNAMES = "Trainers are not found by these usernames: %s";

  public TrainerNotFoundException(String username) {
    super(username == null || username.isBlank() ? MESSAGE
        : String.format(MESSAGE_FORMAT_USERNAME, username));
  }

  public TrainerNotFoundException(List<String> usernames) {
    super(String.format(MESSAGE_FORMAT_USERNAMES, usernames));
  }
}
