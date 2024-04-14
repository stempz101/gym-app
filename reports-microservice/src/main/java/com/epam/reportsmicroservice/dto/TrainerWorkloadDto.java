package com.epam.reportsmicroservice.dto;

import java.time.Month;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadDto {

  private String username;
  private String firstName;
  private String lastName;
  private boolean isActive;
  private Integer year;
  private Month month;
  private Long duration;
}
