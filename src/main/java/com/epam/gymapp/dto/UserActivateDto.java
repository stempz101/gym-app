package com.epam.gymapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivateDto {

  @NotBlank(message = "{validation.user.not-blank.username}")
  private String username;

  @NotNull(message = "{validation.user.not-null.is-active}")
  private Boolean isActive;
}
