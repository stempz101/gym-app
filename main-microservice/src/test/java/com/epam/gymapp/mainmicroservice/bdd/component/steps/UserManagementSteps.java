package com.epam.gymapp.mainmicroservice.bdd.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.epam.gymapp.mainmicroservice.dto.TraineeCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerCreateDto;
import com.epam.gymapp.mainmicroservice.dto.UserCredentialsDto;
import com.epam.gymapp.mainmicroservice.service.TraineeService;
import com.epam.gymapp.mainmicroservice.service.TrainerService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

public class UserManagementSteps {

  @Autowired
  private TraineeService traineeService;

  @Autowired
  private TrainerService trainerService;

  private TraineeCreateDto traineeCreateDto;
  private TrainerCreateDto trainerCreateDto;
  private UserCredentialsDto userCredentialsDto;

  @Given("the trainee data as: {string}, {string}, {localDate}, {string}")
  public void the_trainee_data_as(String firstName, String lastName, LocalDate dateOfBirth, String address) {
    traineeCreateDto = new TraineeCreateDto(firstName, lastName, dateOfBirth, address);
  }

  @Given("the trainer data as: {string}, {string}, {string}")
  public void the_trainer_data_as(String firstName, String lastName, String specialization) {
    trainerCreateDto = new TrainerCreateDto(firstName, lastName, specialization);
  }

  @When("a request to create the trainee is made")
  public void a_request_to_create_the_trainee_is_made() {
    userCredentialsDto = traineeService.createTrainee(traineeCreateDto);
  }

  @When("a request to create the trainer is made")
  public void a_request_to_create_the_trainer_is_made() {
    userCredentialsDto = trainerService.createTrainer(trainerCreateDto);
  }

  @Then("the response should contain the username {string}")
  public void the_response_should_contain_the_username(String username) {
    assertEquals(username, userCredentialsDto.getUsername());
  }

  @Then("the password in the response has a length of {int}")
  public void the_password_in_the_response_has_a_length_of(Integer passwordLength) {
    assertEquals(passwordLength, userCredentialsDto.getPassword().length);
  }

  @Then("the response includes a token")
  public void the_response_includes_a_token() {
    assertFalse(userCredentialsDto.getToken().isBlank());
  }
}
