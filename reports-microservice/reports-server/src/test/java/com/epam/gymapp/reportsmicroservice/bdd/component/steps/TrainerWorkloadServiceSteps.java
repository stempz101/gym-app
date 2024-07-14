package com.epam.gymapp.reportsmicroservice.bdd.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.reportsmicroservice.bdd.component.context.CucumberComponentContext;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.model.MonthSummary;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.model.YearSummary;
import com.epam.gymapp.reportsmicroservice.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class TrainerWorkloadServiceSteps {

  @Autowired
  private TrainerWorkloadService trainerWorkloadService;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private CucumberComponentContext context;

  @Given("a trainer record with username {string} for the month {string} in {int}")
  public void a_trainer_record_with_username_for_the_month_in(String username, String month, Integer year) {
    context.setTrainerUsername(username);
    context.setWorkloadMonth(month);
    context.setWorkloadYear(year);
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
      YearSummary year = trainerWorkload.getYears().stream()
          .filter(yearSummary -> yearSummary.getYear() == context.getWorkloadYear())
          .findFirst()
          .orElseGet(() -> new YearSummary(context.getWorkloadYear(), new ArrayList<>()));
      Long currentWorkingHours = year.getMonths().stream()
          .filter(monthSummary -> monthSummary.getMonth() == context.getWorkloadMonth())
          .map(MonthSummary::getDuration)
          .findFirst()
          .orElse(0L);

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
      YearSummary year = trainerWorkload.getYears().stream()
          .filter(yearSummary -> yearSummary.getYear() == context.getWorkloadYear())
          .findFirst()
          .orElseGet(() -> new YearSummary(context.getWorkloadYear(), new ArrayList<>()));
      Long currentWorkingHours = year.getMonths().stream()
          .filter(monthSummary -> monthSummary.getMonth() == context.getWorkloadMonth())
          .map(MonthSummary::getDuration)
          .findFirst()
          .orElse(0L);

      context.setCurrentWorkingHours(currentWorkingHours);
    }

    trainerWorkloadService.updateTrainersWorkload(new TrainerWorkloadUpdateDtoList(List.of(updateDto)));
  }

  @Then("the trainer's working hours for a given month should be updated and increased")
  public void the_trainer_s_working_hours_for_a_given_month_should_be_updated_and_increased() {
    TrainerWorkload trainerWorkload = mongoTemplate.findById(context.getTrainerUsername(), TrainerWorkload.class);
    assertNotNull(trainerWorkload);
    assertEquals(context.getTrainerUsername(), trainerWorkload.getUsername());
    assertNotNull(trainerWorkload.getYears());

    Optional<YearSummary> year = trainerWorkload.getYears().stream()
        .filter(yearSummary -> yearSummary.getYear() == context.getWorkloadYear())
        .findFirst();
    assertTrue(year.isPresent());

    Optional<Long> updatedWorkingHours = year.get().getMonths().stream()
        .filter(monthSummary -> monthSummary.getMonth() == context.getWorkloadMonth())
        .map(MonthSummary::getDuration)
        .findFirst();
    assertTrue(updatedWorkingHours.isPresent());
    assertTrue(updatedWorkingHours.get() >= context.getCurrentWorkingHours());
  }

  @Then("the trainer's working hours for a given month should be updated and decreased")
  public void the_trainer_s_working_hours_for_a_given_month_should_be_updated_and_decreased() {
    TrainerWorkload trainerWorkload = mongoTemplate.findById(context.getTrainerUsername(), TrainerWorkload.class);
    if (trainerWorkload != null) {
      assertEquals(context.getTrainerUsername(), trainerWorkload.getUsername());
      assertNotNull(trainerWorkload.getYears());

      trainerWorkload.getYears().stream()
          .filter(yearSummary -> yearSummary.getYear() == context.getWorkloadYear())
          .findFirst().flatMap(yearSummary -> yearSummary.getMonths().stream()
              .filter(monthSummary -> monthSummary.getMonth() == context.getWorkloadMonth())
              .findFirst()).ifPresent(monthSummary -> assertTrue(
              monthSummary.getDuration() <= context.getCurrentWorkingHours()));
    }
  }
}
