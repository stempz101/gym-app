package com.epam.gymapp.model.builder;

import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.TrainingType;

public class TrainerBuilder {

  private Long userId;
  private String firstName;
  private String lastName;
  private String username;
  private char[] password;
  private TrainingType specialization;
  private boolean isActive;

  public TrainerBuilder userId(Long userId) {
    this.userId = userId;
    return this;
  }

  public TrainerBuilder firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public TrainerBuilder lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public TrainerBuilder username(String username) {
    this.username = username;
    return this;
  }

  public TrainerBuilder password(char[] password) {
    this.password = password;
    return this;
  }

  public TrainerBuilder specialization(TrainingType specialization) {
    this.specialization = specialization;
    return this;
  }

  public TrainerBuilder isActive(boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public Trainer build() {
    return new Trainer(firstName, lastName, username, password, isActive, userId, specialization);
  }
}
