package com.epam.gymapp.dto;

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

  private String username;
  private String firstName;
  private String lastName;
  private Boolean isActive;
  private LocalDate trainingDate;
  private Long trainingDuration;
  private ActionType actionType;

  public enum ActionType {
    ADD, DELETE
  }
}
