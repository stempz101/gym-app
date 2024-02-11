package com.epam.gymapp.dto;

import com.epam.gymapp.model.TrainingType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainerCreateDto {

  private String firstName;
  private String lastName;
  private TrainingType specialization;
}
