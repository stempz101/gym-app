package com.epam.gymapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
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
public class TrainerUpdateDto {

  @NotBlank(message = "Username must be specified")
  private String username;

  @NotBlank(message = "Trainer's first name must be specified")
  private String firstName;

  @NotBlank(message = "Trainer's last name must be specified")
  private String lastName;

  @JsonProperty(access = Access.READ_ONLY)
  private String specialization;

  @NotNull(message = "Active status must be specified")
  private Boolean isActive;
}
