package com.epam.gymapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraineeShortInfoDto {

  private String username;
  private String firstName;
  private String lastName;
}
