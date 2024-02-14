package com.epam.gymapp.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraineeCreateDto {

  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String address;
}
