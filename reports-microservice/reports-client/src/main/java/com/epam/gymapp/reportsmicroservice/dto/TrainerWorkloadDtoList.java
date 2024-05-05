package com.epam.gymapp.reportsmicroservice.dto;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkloadDtoList {

  private List<TrainerWorkloadDto> items;

  public static TrainerWorkloadDtoList getFallbackList(
      int year, int month, String firstName, String lastName) {

    return new TrainerWorkloadDtoList(
        Collections.singletonList(TrainerWorkloadDto
            .getFallbackObject(year, month, firstName, lastName)));
  }
}
