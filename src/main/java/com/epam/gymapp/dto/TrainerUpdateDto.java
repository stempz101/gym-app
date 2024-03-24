package com.epam.gymapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
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
public class TrainerUpdateDto {

  @NotBlank(message = "{validation.user.not-blank.username}")
  private String username;

  @NotBlank(message = "{validation.trainer.not-blank.first-name}")
  private String firstName;

  @NotBlank(message = "{validation.trainer.not-blank.last-name}")
  private String lastName;

  @JsonProperty(access = Access.READ_ONLY)
  private String specialization;

  @NotNull(message = "{validation.user.not-null.is-active}")
  private Boolean isActive;
}
