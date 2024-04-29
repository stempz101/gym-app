package com.epam.gymapp.mainmicroservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainerShortInfoDto {

  private String username;
  private String firstName;
  private String lastName;
  private String specialization;
}
