package com.epam.gymapp.model;

import java.time.Duration;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Training {

  private Long id;
  private Long traineeId;
  private Long trainerId;
  private String name;
  private TrainingType type;
  private LocalDate date;
  private Duration duration;
}
