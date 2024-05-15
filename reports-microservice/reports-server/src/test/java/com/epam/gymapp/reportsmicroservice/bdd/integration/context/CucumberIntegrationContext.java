package com.epam.gymapp.reportsmicroservice.bdd.integration.context;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("cucumber-glue")
public class CucumberIntegrationContext {

  private String trainerUsername;
  private String trainerFirstName;
  private String trainerLastName;
  private Month workloadMonth;
  private int workloadYear;

  private UUID correlationId;

  private TrainerWorkloadUpdateDtoList workloadUpdateDtoList;
  private List<TrainerWorkload> expectedWorkloadList;

  public void setWorkloadMonth(String month) {
    this.workloadMonth = Month.valueOf(month);
  }
}
