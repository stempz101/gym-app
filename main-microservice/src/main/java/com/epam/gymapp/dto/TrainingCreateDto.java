package com.epam.gymapp.dto;

import jakarta.validation.constraints.Min;
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
public class TrainingCreateDto {

  @NotBlank(message = "{validation.trainee.not-blank.username}")
  private String traineeUsername;

  @NotBlank(message = "{validation.trainer.not-blank.username}")
  private String trainerUsername;

  @NotBlank(message = "{validation.training.not-blank.name}")
  private String name;

  @NotNull(message = "{validation.training.not-null.date}")
  private LocalDate date;

  @NotNull(message = "{validation.training.not-null.duration}")
  @Min(value = 0, message = "{validation.training.min.duration}")
  private Integer duration;
}
