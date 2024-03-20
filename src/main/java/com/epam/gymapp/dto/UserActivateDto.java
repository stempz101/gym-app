package com.epam.gymapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivateDto {

  @NotBlank(message = "Username must be specified")
  private String username;

  @NotNull(message = "Activation status must be specified")
  private Boolean isActive;
}
