package com.epam.gymapp.model.builder;

import com.epam.gymapp.model.Trainee;
import java.time.LocalDate;

public class TraineeBuilder {

  private Long userId;
  private String firstName;
  private String lastName;
  private String username;
  private char[] password;
  private LocalDate dateOfBirth;
  private String address;
  private boolean isActive;

  public TraineeBuilder userId(Long userId) {
    this.userId = userId;
    return this;
  }

  public TraineeBuilder firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public TraineeBuilder lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public TraineeBuilder username(String username) {
    this.username = username;
    return this;
  }

  public TraineeBuilder password(char[] password) {
    this.password = password;
    return this;
  }

  public TraineeBuilder dateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public TraineeBuilder address(String address) {
    this.address = address;
    return this;
  }

  public TraineeBuilder isActive(boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public Trainee build() {
    return new Trainee(firstName, lastName, username, password, isActive, userId, dateOfBirth,
        address);
  }
}
