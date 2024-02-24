package com.epam.gymapp.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraineeInfoDto {

  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String address;
  private List<TrainerShortInfoDto> trainers;
}
