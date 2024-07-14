package com.epam.gymapp.reportsmicroservice.bdd.component.context;

import java.time.Month;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("cucumber-glue")
public class CucumberComponentContext {

  private String trainerUsername;
  private String trainerFirstName;
  private String trainerLastName;
  private int workloadMonth;
  private int workloadYear;
  private long currentWorkingHours;

  public void setWorkloadMonth(String month) {
    this.workloadMonth = Month.valueOf(month).getValue();
  }
}
