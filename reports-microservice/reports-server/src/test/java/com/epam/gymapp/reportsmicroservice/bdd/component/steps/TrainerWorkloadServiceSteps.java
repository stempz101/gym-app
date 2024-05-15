package com.epam.gymapp.reportsmicroservice.bdd.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.reportsmicroservice.bdd.component.context.CucumberComponentContext;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TrainerWorkloadServiceSteps {

  @Autowired
  private TrainerWorkloadService trainerWorkloadService;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private CucumberComponentContext context;

  private TrainerWorkloadDtoList trainerWorkloadDtoList;

  @Given("a trainer record with username {string} for the month {string} in {int}")
  public void a_trainer_record_with_username_for_the_month_in(String username, String month, Integer year) {
    context.setTrainerUsername(username);
    context.setWorkloadMonth(month);
    context.setWorkloadYear(year);
  }

  @Given("the workload month {string} {int}")
  public void the_workload_month(String month, Integer year) {
    context.setWorkloadMonth(month);
    context.setWorkloadYear(year);
  }

  @When("a request to retrieve workload by name {string} {string} for {string} {int} is made")
  public void a_request_to_retrieve_workload_by_name_for_is_made(
      String firstName, String lastName, String month, Integer year) {

    trainerWorkloadDtoList = trainerWorkloadService.retrieveTrainersWorkloadForMonth(
        year, Month.valueOf(month).getValue(), firstName, lastName);
  }

  @When("a request to retrieve workload for all trainers for {string} {int} is made")
  public void a_request_to_retrieve_workload_for_all_trainers_for_is_made(String month, Integer year) {
    trainerWorkloadDtoList = trainerWorkloadService.retrieveTrainersWorkloadForMonth(
        year, Month.valueOf(month).getValue(), null, null);
  }

  @When("a request to add {long} working hours for the trainer for a given month is made")
  public void a_request_to_add_working_duration_for_the_trainer_for_a_given_month_is_made(Long duration) {
    TrainerWorkload trainerWorkload = mongoTemplate.findById(context.getTrainerUsername(), TrainerWorkload.class);
    TrainerWorkloadUpdateDto updateDto = TrainerWorkloadUpdateDto.builder()
        .username(context.getTrainerUsername())
        .trainingDate(LocalDate.of(context.getWorkloadYear(), context.getWorkloadMonth(), 1))
        .trainingDuration(duration)
        .actionType(ActionType.ADD)
        .build();

    if (trainerWorkload != null) {
      Map<Month, Long> year = trainerWorkload.getYears().getOrDefault(context.getWorkloadYear(), new HashMap<>());
      Long currentWorkingHours = year.getOrDefault(context.getWorkloadMonth(), 0L);
      context.setCurrentWorkingHours(currentWorkingHours);
    }

    trainerWorkloadService.updateTrainersWorkload(new TrainerWorkloadUpdateDtoList(List.of(updateDto)));
  }

  @When("a request to subtract {long} working hours for the trainer for a given month is made")
  public void a_request_to_subtract_working_hours_for_the_trainer_for_a_given_month_is_made(Long duration) {
    TrainerWorkload trainerWorkload = mongoTemplate.findById(context.getTrainerUsername(), TrainerWorkload.class);
    TrainerWorkloadUpdateDto updateDto = TrainerWorkloadUpdateDto.builder()
        .username(context.getTrainerUsername())
        .trainingDate(LocalDate.of(context.getWorkloadYear(), context.getWorkloadMonth(), 1))
        .trainingDuration(duration)
        .actionType(ActionType.DELETE)
        .build();

    if (trainerWorkload != null) {
      Map<Month, Long> year = trainerWorkload.getYears().getOrDefault(context.getWorkloadYear(), new HashMap<>());
      Long currentWorkingHours = year.getOrDefault(context.getWorkloadMonth(), 0L);
      context.setCurrentWorkingHours(currentWorkingHours);
    }

    trainerWorkloadService.updateTrainersWorkload(new TrainerWorkloadUpdateDtoList(List.of(updateDto)));
  }

  @Then("the trainer record with given username for a given month should be included in the response")
  public void the_trainer_record_with_given_username_should_be_included_in_the_response() {
    assertNotNull(trainerWorkloadDtoList);
    assertNotNull(trainerWorkloadDtoList.getItems());
    assertFalse(trainerWorkloadDtoList.getItems().isEmpty());

    TrainerWorkloadDto trainerWorkloadDto = trainerWorkloadDtoList.getItems().get(0);

    assertEquals(context.getTrainerUsername(), trainerWorkloadDto.getUsername());
    assertEquals(context.getWorkloadMonth(), trainerWorkloadDto.getMonth());
    assertEquals(context.getWorkloadYear(), trainerWorkloadDto.getYear());
  }

  @Then("the trainer record for a given month should be included in the response")
  public void the_trainer_record_should_be_included_in_the_response() {
    assertNotNull(trainerWorkloadDtoList);
    assertNotNull(trainerWorkloadDtoList.getItems());
    assertFalse(trainerWorkloadDtoList.getItems().isEmpty());

    TrainerWorkloadDto trainerWorkloadDto = trainerWorkloadDtoList.getItems().get(0);
    assertEquals(context.getWorkloadMonth(), trainerWorkloadDto.getMonth());
    assertEquals(context.getWorkloadYear(), trainerWorkloadDto.getYear());
  }

  @Then("the trainer's working hours for a given month should be updated and increased")
  public void the_trainer_s_working_hours_for_a_given_month_should_be_updated_and_increased() {
    TrainerWorkload trainerWorkload = mongoTemplate.findById(context.getTrainerUsername(), TrainerWorkload.class);
    assertNotNull(trainerWorkload);
    assertEquals(context.getTrainerUsername(), trainerWorkload.getUsername());
    assertNotNull(trainerWorkload.getYears());

    Map<Month, Long> year = trainerWorkload.getYears().get(context.getWorkloadYear());
    assertNotNull(year);

    Long updatedWorkingHours = year.get(context.getWorkloadMonth());
    assertNotNull(updatedWorkingHours);
    assertTrue(updatedWorkingHours >= context.getCurrentWorkingHours());
  }

  @Then("the trainer's working hours for a given month should be updated and decreased")
  public void the_trainer_s_working_hours_for_a_given_month_should_be_updated_and_decreased() {
    TrainerWorkload trainerWorkload = mongoTemplate.findById(context.getTrainerUsername(), TrainerWorkload.class);
    if (trainerWorkload != null) {
      assertEquals(context.getTrainerUsername(), trainerWorkload.getUsername());
      assertNotNull(trainerWorkload.getYears());

      Map<Month, Long> year = trainerWorkload.getYears().get(context.getWorkloadYear());
      if (year != null) {
        Long updatedWorkingHours = year.get(context.getWorkloadMonth());
        if (updatedWorkingHours != null) {
          assertTrue(updatedWorkingHours <= context.getCurrentWorkingHours());
        }
      }
    }
  }
}
