package com.epam.gymapp.mainmicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerCreateDto {

  @NotBlank(message = "{validation.trainer.not-blank.first-name}")
  private String firstName;

  @NotBlank(message = "{validation.trainer.not-blank.last-name}")
  private String lastName;

  @NotBlank(message = "{validation.trainer.not-blank.specialization}")
  private String specialization;
}
