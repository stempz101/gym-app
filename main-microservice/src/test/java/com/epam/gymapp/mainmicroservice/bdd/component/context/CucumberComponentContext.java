package com.epam.gymapp.mainmicroservice.bdd.component.context;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("cucumber-glue")
public class CucumberComponentContext {

  private String traineeUsername;
  private String trainerUsername;

  private LocalDate fromDate;
  private LocalDate toDate;
  private String traineeName;
  private String trainerName;
  private String trainingType;

  private Exception exception;
}
