package com.epam.gymapp.mainmicroservice.model.custom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkload {

  private String username;
  private String firstName;
  private String lastName;
  private Boolean isActive;
  private Integer trainingYear;
  private Integer trainingMonth;
  private Long trainingDuration;
}
