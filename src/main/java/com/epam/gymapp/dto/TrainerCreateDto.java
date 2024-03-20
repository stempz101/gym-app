package com.epam.gymapp.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerCreateDto {

  @NotBlank(message = "Trainer's first name must be specified")
  private String firstName;

  @NotBlank(message = "Trainer's last name must be specified")
  private String lastName;

  @NotBlank(message = "Trainer's specialization must be specified")
  private String specialization;
}
