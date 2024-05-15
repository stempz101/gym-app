package com.epam.gymapp.mainmicroservice.bdd.integration.steps;

import com.epam.gymapp.mainmicroservice.bdd.integration.contex.CucumberIntegrationContext;
import com.epam.gymapp.mainmicroservice.dto.TrainingCreateDto;
import io.cucumber.java.en.Given;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class TrainingSteps {

  @Autowired
  private CucumberIntegrationContext context;

  @Given("the training data for adding as below:")
  public void the_training_data_for_adding_as_below(Map<String, String> map) {
    String traineeUsername = map.get("traineeUsername");
    String trainerUsername = map.get("trainerUsername");
    String trainingName = map.get("trainingName");

    LocalDate trainingDate = map.get("trainingDate") != null
        ? LocalDate.parse(map.get("trainingDate"), DateTimeFormatter.ISO_LOCAL_DATE)
        : null;
    Integer trainingDuration = map.get("trainingDuration") != null
        ? Integer.parseInt(map.get("trainingDuration"))
        : null;

    TrainingCreateDto trainingCreateDto = new TrainingCreateDto(
        traineeUsername, trainerUsername, trainingName, trainingDate, trainingDuration);

    context.setBody(trainingCreateDto);
  }
}
