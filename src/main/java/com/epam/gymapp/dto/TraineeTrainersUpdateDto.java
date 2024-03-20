package com.epam.gymapp.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeTrainersUpdateDto {

  @NotBlank(message = "Trainee's username must be specified")
  private String traineeUsername;

  @NotNull(message = "Trainers list must be filled")
  @Size(min = 1, message = "At least one trainer must be specified in list")
  private List<@NotBlank(message = "Trainer's username must be specified in list") String> trainerUsernames;
}
