package com.epam.gymapp.mainmicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeUpdateDto {

  @NotBlank(message = "{validation.user.not-blank.username}")
  private String username;

  @NotBlank(message = "{validation.trainee.not-blank.first-name}")
  private String firstName;

  @NotBlank(message = "{validation.trainee.not-blank.last-name}")
  private String lastName;

  private LocalDate dateOfBirth;

  private String address;

  @NotNull(message = "{validation.user.not-null.is-active}")
  private Boolean isActive;
}
