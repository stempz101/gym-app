package com.epam.gymapp.model;

import com.epam.gymapp.model.builder.TrainerBuilder;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Trainer extends User {

  private Long userId;
  private TrainingType specialization;

  public Trainer(String firstName, String lastName, String username, char[] password,
      boolean isActive, Long userId, TrainingType specialization) {
    super(firstName, lastName, username, password, isActive);
    this.userId = userId;
    this.specialization = specialization;
  }

  public static TrainerBuilder builder() {
    return new TrainerBuilder();
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
    Trainer trainer = (Trainer) o;
    return Objects.equals(userId, trainer.userId) && Objects.equals(
        specialization, trainer.specialization);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), userId, specialization);
  }

  @Override
  public String toString() {
    return "Trainer(" +
        "userId=" + userId +
        ", firstName=" + firstName +
        ", lastName=" + lastName +
        ", specialization=" + specialization +
        ')';
  }
}
