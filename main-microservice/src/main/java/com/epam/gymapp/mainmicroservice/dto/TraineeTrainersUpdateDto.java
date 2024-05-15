package com.epam.gymapp.mainmicroservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainersUpdateDto {

  @NotBlank(message = "{validation.trainee.not-blank.username}")
  private String traineeUsername;

  @NotNull(message = "{validation.trainee.not-null.trainers}")
  @Size(min = 1, message = "{validation.trainee.size.trainers}")
  private List<@NotBlank(message = "{validation.trainee.not-blank.trainers.username}") String> trainerUsernames;
}
