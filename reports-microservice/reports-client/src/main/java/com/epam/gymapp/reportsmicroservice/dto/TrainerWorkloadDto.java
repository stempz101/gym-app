package com.epam.gymapp.reportsmicroservice.dto;

import java.time.Month;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadDto {

  private String username;
  private String firstName;
  private String lastName;
  private boolean isActive;
  private Integer year;
  private Month month;
  private Long duration;

  public static TrainerWorkloadDto getFallbackObject(
      int year, int month, String firstName, String lastName) {

    return TrainerWorkloadDto.builder()
        .username("N/A")
        .firstName(firstName != null && !firstName.isBlank() ? firstName : "N/A")
        .lastName(lastName != null && !lastName.isBlank() ? lastName : "N/A")
        .isActive(false)
        .year(year)
        .month(Month.of(month))
        .duration(0L)
        .build();
  }
}
