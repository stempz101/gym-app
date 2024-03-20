package com.epam.gymapp.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TraineeCreateDto {

  @NotBlank(message = "Trainee's first name must be specified")
  private String firstName;

  @NotBlank(message = "Trainee's last name must be specified")
  private String lastName;

  private LocalDate dateOfBirth;

  private String address;
}
