package com.epam.gymapp.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingCreateDto {

  private String traineeUsername;
  private String trainerUsername;
  private String name;
  private LocalDate date;
  private Integer duration;
}
