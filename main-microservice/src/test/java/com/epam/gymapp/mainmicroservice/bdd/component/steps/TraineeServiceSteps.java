package com.epam.gymapp.mainmicroservice.bdd.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.mainmicroservice.bdd.component.context.CucumberComponentContext;
import com.epam.gymapp.mainmicroservice.dto.TraineeInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.exception.TraineeNotFoundException;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.service.TraineeService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class TraineeServiceSteps {

  @Autowired
  private TraineeService traineeService;

  @Autowired
  private CucumberComponentContext context;

  private List<String> nonExistentTrainers;

  private TraineeUpdateDto traineeUpdateDto;
  private TraineeTrainersUpdateDto traineeTrainersUpdateDto;
  private List<TraineeInfoDto> traineeInfoList;
  private TraineeInfoDto traineeInfo;
  private List<TrainingInfoDto> trainingInfoList;
  private List<TrainerShortInfoDto> trainerShortInfoList;

  @Given("a trainee exists with username {string}")
  public void a_trainee_exists_with_username(String username) {
    context.setTraineeUsername(username);
  }

  @Given("there is no trainee with username {string}")
  public void there_is_no_trainee_with_username(String username) {
    context.setTraineeUsername(username);
  }

  @Given("the trainee {string} is selected")
  public void the_trainee_is_selected(String username) {
    context.setTraineeUsername(username);
    traineeInfo = traineeService.selectTrainee(username);
  }

  @Given("data to update the trainee: first name {string}, address {string}")
  public void data_to_update_the_trainee_first_name_address(String firstName,
      String address) {

    traineeUpdateDto = TraineeUpdateDto.builder()
        .username(context.getTraineeUsername())
        .firstName(firstName)
        .lastName(traineeInfo.getLastName())
        .dateOfBirth(traineeInfo.getDateOfBirth())
        .address(address)
        .isActive(traineeInfo.isActive())
        .build();
  }

  @Given("a non-existent trainee {string} is predefined for update")
  public void a_non_existent_trainee_is_predefined_for_update(String username) {
    context.setTraineeUsername(username);
    traineeUpdateDto = TraineeUpdateDto.builder().username(username).build();
  }

  @Given("parameters to filter trainee's trainings: From Date = {localDate}, To Date = {localDate}, Trainer Name = {string}, Training Type = {string}")
  public void parameters_to_filter_trainee_s_trainings_from_date_to_date_trainer_name_training_type(
      LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
    context.setFromDate(fromDate);
    context.setToDate(toDate);
    context.setTrainerName(trainerName);
    context.setTrainingType(trainingType);
  }

  @Given("a list of trainers is provided with the following usernames:")
  public void a_list_of_trainers_is_provided_with_the_following_usernames(List<String> trainerUsernames) {
    traineeTrainersUpdateDto = new TraineeTrainersUpdateDto(
        context.getTraineeUsername(), trainerUsernames);
  }

  @Given("a non-existent trainers are predefined for trainee's trainer list update:")
  public void a_non_existent_trainers_are_predefined_for_trainee_s_trainer_list_update(List<String> trainerUsernames) {
    nonExistentTrainers = trainerUsernames;
  }

  @When("a request to fetch all trainees is made")
  public void a_request_to_fetch_all_trainees_is_made() {
    traineeInfoList = traineeService.selectTrainees();
  }

  @When("a request to fetch the trainee data is made")
  public void a_request_to_fetch_the_trainee_data_is_made() {
    traineeInfo = traineeService.selectTrainee(context.getTraineeUsername());
  }

  @When("an attempt is made to fetch the trainee's data")
  public void an_attempt_is_made_to_fetch_the_trainee_s_data() {
    Exception exception = assertThrows(TraineeNotFoundException.class,
        () -> traineeService.selectTrainee(context.getTraineeUsername()));

    context.setException(exception);
  }

  @When("a request to update the trainee data is made")
  public void a_request_to_update_the_trainee_data_is_made() {
    traineeInfo = traineeService.updateTrainee(traineeUpdateDto);
  }

  @When("an attempt is made to update the trainee's data")
  public void an_attempt_is_made_to_update_the_trainee_s_data() {
    Exception exception = assertThrows(TraineeNotFoundException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));

    context.setException(exception);
  }

  @When("a request to delete the trainee is made")
  public void a_request_to_delete_the_trainee_is_made() {
    traineeService.deleteTrainee(context.getTraineeUsername());
  }

  @When("an attempt is made to delete the trainee")
  public void an_attempt_is_made_to_delete_the_trainee() {
    Exception exception = assertThrows(TraineeNotFoundException.class,
        () -> traineeService.deleteTrainee(context.getTraineeUsername()));

    context.setException(exception);
  }

  @When("a request is made to fetch trainee's trainings with the parameters")
  public void a_request_is_made_to_fetch_trainee_s_trainings_with_the_parameters() {
    String traineeUsername = context.getTraineeUsername();
    LocalDate fromDate = context.getFromDate();
    LocalDate toDate = context.getToDate();
    String trainerName = context.getTrainerName();
    String trainingType = context.getTrainingType();

    trainingInfoList = traineeService.findTraineeTrainings(
        traineeUsername, fromDate, toDate, trainerName, trainingType);
  }

  @When("a request to update the trainee's trainer list is made")
  public void a_request_to_update_the_trainee_s_trainer_list_is_made() {
    trainerShortInfoList = traineeService.updateTrainerList(traineeTrainersUpdateDto);
  }

  @When("an attempt is made to update the trainer list of a non-existent trainee")
  public void an_attempt_is_made_to_update_the_trainer_list_of_a_non_existent_trainee() {
    Exception exception = assertThrows(TraineeNotFoundException.class,
        () -> traineeService.updateTrainerList(traineeTrainersUpdateDto));

    context.setException(exception);
  }

  @When("an attempt is made to update the trainee's trainer list with non-existing trainers")
  public void an_attempt_is_made_to_update_the_trainee_s_trainer_list_with_non_existing_trainers() {
    Exception exception = assertThrows(TrainerNotFoundException.class,
        () -> traineeService.updateTrainerList(traineeTrainersUpdateDto));

    context.setException(exception);
  }

  @Then("a list of all trainees is returned")
  public void a_list_of_all_trainees_is_returned() {
    assertNotNull(traineeInfoList);
    assertFalse(traineeInfoList.isEmpty());
  }

  @Then("the expected trainee data is returned")
  public void the_expected_trainee_data_is_returned() {
    assertNotNull(traineeInfo);
  }

  @Then("a message should be returned stating that the trainee was not found")
  public void a_message_should_be_returned_stating_that_the_trainee_was_not_found() {
    String message = String.format("Trainee with username '%s' is not found",
        context.getTraineeUsername());

    assertEquals(message, context.getException().getMessage());
  }

  @Then("the updated trainee data is returned")
  public void the_updated_trainee_data_is_returned() {
    assertNotNull(traineeInfo);
    assertEquals(traineeUpdateDto.getFirstName(), traineeInfo.getFirstName());
    assertEquals(traineeUpdateDto.getLastName(), traineeInfo.getLastName());
    assertEquals(traineeUpdateDto.getDateOfBirth(), traineeInfo.getDateOfBirth());
    assertEquals(traineeUpdateDto.getAddress(), traineeInfo.getAddress());
    assertEquals(traineeUpdateDto.getIsActive(), traineeInfo.isActive());
  }

  @Then("the trainee should no longer exist")
  public void the_trainee_should_no_longer_exist() {
    assertThrows(TraineeNotFoundException.class,
        () -> traineeService.selectTrainee(context.getTraineeUsername()));
  }

  @Then("all trainings associated with the trainee should be removed")
  public void all_trainings_associated_with_the_trainee_should_be_removed() {
    List<TrainingInfoDto> traineeTrainings = traineeService.findTraineeTrainings(
        context.getTraineeUsername(), null, null, null, null);

    assertTrue(traineeTrainings.isEmpty());
  }

  @Then("the filtered trainee's trainings are returned")
  public void the_filtered_trainee_s_trainings_are_returned() {
    assertFalse(trainingInfoList.isEmpty());
  }

  @Then("the updated list of trainers is returned")
  public void the_updated_list_of_trainers_is_returned() {
    assertFalse(trainerShortInfoList.isEmpty());
    assertEquals(traineeTrainersUpdateDto.getTrainerUsernames().size(), trainerShortInfoList.size());

    for (int i = 0; i < trainerShortInfoList.size(); i++) {
      assertEquals(traineeTrainersUpdateDto.getTrainerUsernames().get(i),
          trainerShortInfoList.get(i).getUsername());
    }
  }

  @Then("a message should be returned stating that trainers were not found")
  public void a_message_should_be_returned_stating_that_trainers_were_not_found() {
    String message = String.format("Trainers are not found by these usernames: %s",
        nonExistentTrainers);

    assertEquals(message, context.getException().getMessage());
  }
}
