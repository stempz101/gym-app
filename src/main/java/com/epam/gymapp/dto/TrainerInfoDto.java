package com.epam.gymapp.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainerInfoDto {

  private String firstName;
  private String lastName;
  private String specialization;
  private List<TraineeShortInfoDto> trainees;
}
