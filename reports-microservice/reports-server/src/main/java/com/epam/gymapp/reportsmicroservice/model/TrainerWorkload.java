package com.epam.gymapp.reportsmicroservice.model;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkload {

  @Id
  private String username;

  @Indexed
  private String firstName;

  @Indexed
  private String lastName;

  private boolean isActive;

  private Map<Integer, Map<Month, Long>> years;

  public Map<Integer, Map<Month, Long>> getYears() {
    if (years == null) {
      years = new HashMap<>();
    }
    return years;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TrainerWorkload that = (TrainerWorkload) object;
    return isActive == that.isActive && Objects.equals(username, that.username)
        && Objects.equals(firstName, that.firstName) && Objects.equals(lastName,
        that.lastName) && Objects.equals(years, that.years);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, firstName, lastName, isActive, years);
  }
}
