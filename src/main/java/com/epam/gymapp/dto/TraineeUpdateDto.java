package com.epam.gymapp.dto;

import java.time.LocalDate;
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
public class TraineeUpdateDto {

  @NotBlank(message = "Username must be specified")
  private String username;

  @NotBlank(message = "Trainee's first name must be specified")
  private String firstName;

  @NotBlank(message = "Trainee's last name must be specified")
  private String lastName;

  private LocalDate dateOfBirth;

  private String address;

  @NotNull(message = "Active status must be specified")
  private Boolean isActive;
}
