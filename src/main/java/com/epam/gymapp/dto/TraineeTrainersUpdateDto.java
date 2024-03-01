package com.epam.gymapp.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraineeTrainersUpdateDto {

  private String traineeUsername;
  private List<String> trainerUsernames;
}
