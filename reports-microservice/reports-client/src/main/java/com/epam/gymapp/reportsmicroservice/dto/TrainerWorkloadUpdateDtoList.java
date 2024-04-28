package com.epam.gymapp.reportsmicroservice.dto;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadUpdateDtoList {

  @Valid
  private List<TrainerWorkloadUpdateDto> items;
}
