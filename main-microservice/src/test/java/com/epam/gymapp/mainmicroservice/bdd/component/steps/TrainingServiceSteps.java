package com.epam.gymapp.mainmicroservice.bdd.component.steps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.gymapp.mainmicroservice.bdd.component.context.CucumberComponentContext;
import com.epam.gymapp.mainmicroservice.dto.TrainingCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.exception.TraineeNotFoundException;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.mainmicroservice.repository.TrainingRepository;
import com.epam.gymapp.mainmicroservice.service.TrainingService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class TrainingServiceSteps {

  @Autowired
  private TrainingService trainingService;

  @Autowired
  private TrainingRepository trainingRepository;

  @Autowired
  private CucumberComponentContext context;

  private TrainingCreateDto trainingCreateDto;
  private List<TrainingInfoDto> trainingInfoList;

  @Given("the training data as: {string}, {localDate}, {int}")
  public void the_training_data_as(String trainingName, LocalDate trainingDate, Integer trainingDuration) {
    trainingCreateDto = TrainingCreateDto.builder()
        .traineeUsername(context.getTraineeUsername())
        .trainerUsername(context.getTrainerUsername())
        .name(trainingName)
        .date(trainingDate)
        .duration(trainingDuration)
        .build();
  }

  @When("a request to add the training is made")
  public void a_request_to_add_the_training_is_made() {
    trainingService.addTraining(trainingCreateDto);
  }

  @When("an attempt is made to add the training with non-existent trainee")
  public void an_attempt_is_made_to_add_the_training_with_non_existent_trainee() {
    Exception exception = assertThrows(TraineeNotFoundException.class,
        () -> trainingService.addTraining(trainingCreateDto));

    context.setException(exception);
  }

  @When("an attempt is made to add the training with non-existent trainer")
  public void an_attempt_is_made_to_add_the_training_with_non_existent_trainer() {
    Exception exception = assertThrows(TrainerNotFoundException.class,
        () -> trainingService.addTraining(trainingCreateDto));

    context.setException(exception);
  }

  @When("a request to fetch all trainings is made")
  public void a_request_to_fetch_all_trainings_is_made() {
    trainingInfoList = trainingService.selectTrainings();
  }

  @Then("the training is created successfully")
  public void the_training_is_created_successfully() {
    verify(trainingRepository, times(1)).save(any(Training.class));
  }

  @Then("a list of all trainings is returned")
  public void a_list_of_all_trainings_is_returned() {
    assertNotNull(trainingInfoList);
    assertFalse(trainingInfoList.isEmpty());
  }
}
