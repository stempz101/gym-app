package com.epam.gymapp.model;

import com.epam.gymapp.model.builder.TraineeBuilder;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Trainee extends User {

  private Long userId;
  private LocalDate dateOfBirth;
  private String address;

  public Trainee(String firstName, String lastName, String username, char[] password,
      boolean isActive, Long userId, LocalDate dateOfBirth, String address) {
    super(firstName, lastName, username, password, isActive);
    this.userId = userId;
    this.dateOfBirth = dateOfBirth;
    this.address = address;
  }

  public static TraineeBuilder builder() {
    return new TraineeBuilder();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Trainee trainee = (Trainee) o;
    return Objects.equals(userId, trainee.userId) && Objects.equals(dateOfBirth,
        trainee.dateOfBirth) && Objects.equals(address, trainee.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), userId, dateOfBirth, address);
  }

  @Override
  public String toString() {
    return "Trainee(" +
        "userId=" + userId +
        ", firstName=" + firstName +
        ", lastName=" + lastName +
        ", dateOfBirth=" + dateOfBirth +
        ", address=" + address +
        ')';
  }
}
