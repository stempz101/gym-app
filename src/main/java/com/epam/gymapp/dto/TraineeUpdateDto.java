package com.epam.gymapp.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraineeUpdateDto {

  private String username;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String address;
}
