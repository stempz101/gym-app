package com.epam.gymapp.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingInfoDto {

  private String name;
  private LocalDate date;
  private String type;
  private int duration;
  private String trainerName;
  private String traineeName;
}
