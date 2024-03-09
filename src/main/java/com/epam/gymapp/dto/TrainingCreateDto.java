package com.epam.gymapp.dto;

import java.time.LocalDate;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCreateDto {

  @NotBlank(message = "Trainee username must be specified")
  private String traineeUsername;

  @NotBlank(message = "Trainer username must be specified")
  private String trainerUsername;

  @NotBlank(message = "Training name must be specified")
  private String name;

  @NotNull(message = "Training date must be specified")
  private LocalDate date;

  @NotNull(message = "Training duration must be specified")
  @Min(value = 0, message = "Training duration must be a non-negative value")
  private Integer duration;
}
