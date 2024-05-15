package com.epam.gymapp.reportsmicroservice.dto;

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
public class TrainerWorkloadUpdateDto {

  @NotBlank(message = "{validation.trainer.not-blank.username}")
  private String username;

  @NotBlank(message = "{validation.trainer.not-blank.first-name}")
  private String firstName;

  @NotBlank(message = "{validation.trainer.not-blank.last-name}")
  private String lastName;

  @NotNull(message = "{validation.trainer.not-null.is-active}")
  private Boolean isActive;

  @NotNull(message = "{validation.training.not-null.date}")
  private LocalDate trainingDate;

  @NotNull(message = "{validation.training.not-null.duration}")
  @Min(value = 0, message = "{validation.training.min.duration}")
  private Long trainingDuration;

  @NotNull(message = "{validation.training.not-null.action-type}")
  private ActionType actionType;

  public enum ActionType {
    ADD, DELETE
  }
}
