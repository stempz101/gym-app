package com.epam.gymapp.dto;

import com.epam.gymapp.model.TrainingType;
import java.time.Duration;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingCreateDto {

  private Long traineeId;
  private Long trainerId;
  private String name;
  private TrainingType type;
  private LocalDate date;
  private Duration duration;
}
