package com.epam.gymapp.mainmicroservice.bdd.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.gymapp.mainmicroservice.bdd.component.context.CucumberComponentContext;
import com.epam.gymapp.mainmicroservice.dto.TrainerInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.service.TrainerService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class TrainerServiceSteps {

  @Autowired
  private TrainerService trainerService;

  @Autowired
  private CucumberComponentContext context;

  private TrainerUpdateDto trainerUpdateDto;
  private TrainerInfoDto trainerInfo;
  private List<TrainerInfoDto> trainerInfoList;
  private List<TrainerShortInfoDto> trainerShortInfoList;
  private List<TrainingInfoDto> trainingInfoList;

  @Given("a trainer exists with username {string}")
  public void a_trainer_exists_with_username(String username) {
    context.setTrainerUsername(username);
  }

  @Given("there is no trainer with username {string}")
  public void there_is_no_trainer_with_username(String username) {
    context.setTrainerUsername(username);
  }

  @Given("the trainer {string} is selected")
  public void the_trainer_is_selected(String username) {
    context.setTrainerUsername(username);
    trainerInfo = trainerService.selectTrainer(username);
  }

  @Given("data to update the trainer: first name {string}, last name {string}")
  public void data_to_update_the_trainer_first_name_last_name(String firstName, String lastName) {
    trainerUpdateDto = TrainerUpdateDto.builder()
        .username(context.getTrainerUsername())
        .firstName(firstName)
        .lastName(lastName)
        .specialization(trainerInfo.getSpecialization())
        .isActive(trainerInfo.isActive())
        .build();
  }

  @Given("a non-existent trainer {string} is predefined for update")
  public void a_non_existent_trainer_is_predefined_for_update(String username) {
    context.setTrainerUsername(username);
    trainerUpdateDto = TrainerUpdateDto.builder().username(username).build();
  }

  @Given("parameters to filter trainer's trainings: From Date = {localDate}, To Date = {localDate}, Trainee Name = {string}")
  public void parameters_to_filter_trainer_s_trainings_from_date_to_date_trainee_name(
      LocalDate fromDate, LocalDate toDate, String traineeName) {
    context.setFromDate(fromDate);
    context.setToDate(toDate);
    context.setTraineeName(traineeName);
  }

  @When("a request to fetch all trainers is made")
  public void a_request_to_fetch_all_trainers_is_made() {
    trainerInfoList = trainerService.selectTrainers();
  }

  @When("a request to fetch the trainer data is made")
  public void a_request_to_fetch_the_trainer_data_is_made() {
    trainerInfo = trainerService.selectTrainer(context.getTrainerUsername());
  }

  @When("an attempt is made to fetch the trainer's data")
  public void an_attempt_is_made_to_fetch_the_trainer_s_data() {
    Exception exception = assertThrows(TrainerNotFoundException.class,
        () -> trainerService.selectTrainer(context.getTrainerUsername()));

    context.setException(exception);
  }

  @When("a request to update the trainer data is made")
  public void a_request_to_update_the_trainer_data_is_made() {
    trainerInfo = trainerService.updateTrainer(trainerUpdateDto);
  }

  @When("an attempt is made to update the trainer's data")
  public void an_attempt_is_made_to_update_the_trainer_s_data() {
    Exception exception = assertThrows(TrainerNotFoundException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));

    context.setException(exception);
  }

  @When("a request is made to fetch trainer's trainings with the parameters")
  public void a_request_is_made_to_fetch_trainer_s_trainings_with_the_parameters() {
    String trainerUsername = context.getTrainerUsername();
    LocalDate fromDate = context.getFromDate();
    LocalDate toDate = context.getToDate();
    String traineeName = context.getTraineeName();

    trainingInfoList = trainerService.findTrainerTrainings(trainerUsername, fromDate, toDate,
        traineeName);
  }

  @When("a request to get unassigned trainee's trainers is made")
  public void a_request_to_get_unassigned_trainee_s_trainers_is_made() {
    trainerShortInfoList = trainerService.findUnassignedTrainers(
        context.getTraineeUsername());
  }

  @Then("a list of all trainers is returned")
  public void a_list_of_all_trainers_is_returned() {
    assertNotNull(trainerInfoList);
    assertFalse(trainerInfoList.isEmpty());
  }

  @Then("the expected trainer data is returned")
  public void the_expected_trainer_data_is_returned() {
    assertNotNull(trainerInfo);
  }

  @Then("a message should be returned stating that the trainer was not found")
  public void a_message_should_be_returned_stating_that_the_trainer_was_not_found() {
    String message = String.format("Trainer with username '%s' is not found",
        context.getTrainerUsername());

    assertEquals(message, context.getException().getMessage());
  }

  @Then("the updated trainer data is returned")
  public void the_updated_trainer_data_is_returned() {
    assertNotNull(trainerInfo);
    assertEquals(trainerUpdateDto.getFirstName(), trainerInfo.getFirstName());
    assertEquals(trainerUpdateDto.getLastName(), trainerInfo.getLastName());
    assertEquals(trainerUpdateDto.getSpecialization(), trainerInfo.getSpecialization());
    assertEquals(trainerUpdateDto.getIsActive(), trainerInfo.isActive());
  }

  @Then("the filtered trainer's trainings are returned")
  public void the_filtered_trainer_s_trainings_are_returned() {
    assertFalse(trainingInfoList.isEmpty());
  }

  @Then("the unassigned trainers are returned")
  public void the_unassigned_trainers_are_returned() {
    assertNotNull(trainerShortInfoList);
    assertFalse(trainerShortInfoList.isEmpty());
  }
}
