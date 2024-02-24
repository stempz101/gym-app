package com.epam.gymapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserActivateDto {

  private String username;
  private Boolean isActive;
}
