package com.epam.gymapp.mainmicroservice.bdd.component.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.gymapp.mainmicroservice.model.TrainingType;
import com.epam.gymapp.mainmicroservice.service.TrainingTypeService;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class TrainingTypeServiceSteps {

  @Autowired
  private TrainingTypeService trainingTypeService;

  private List<TrainingType> trainingTypes;

  @When("a request to fetch all training types is made")
  public void a_request_to_fetch_all_training_types_is_made() {
    trainingTypes = trainingTypeService.selectTrainingTypes();
  }

  @Then("a list of all training types is returned")
  public void a_list_of_all_training_types_is_returned() {
    assertNotNull(trainingTypes);
    assertFalse(trainingTypes.isEmpty());
  }
}
