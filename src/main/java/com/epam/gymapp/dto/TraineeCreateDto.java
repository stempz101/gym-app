package com.epam.gymapp.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TraineeCreateDto {

  @NotBlank(message = "{validation.trainee.not-blank.first-name}")
  private String firstName;

  @NotBlank(message = "{validation.trainee.not-blank.last-name}")
  private String lastName;

  private LocalDate dateOfBirth;

  private String address;
}
