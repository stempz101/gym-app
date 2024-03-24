package com.epam.gymapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class TrainerInfoDto {

  private String username;
  private String firstName;
  private String lastName;
  private String specialization;
  private boolean isActive;
  private List<TraineeShortInfoDto> trainees;
}
