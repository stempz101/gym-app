package com.epam.gymapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class TrainingInfoDto {

  private String name;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;
  private String type;
  private int duration;
  private String trainerName;
  private String traineeName;
}
