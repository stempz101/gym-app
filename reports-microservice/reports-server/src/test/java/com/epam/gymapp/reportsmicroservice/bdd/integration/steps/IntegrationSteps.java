package com.epam.gymapp.reportsmicroservice.bdd.integration.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.epam.gymapp.reportsmicroservice.bdd.integration.context.CucumberIntegrationContext;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.model.MonthSummary;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.model.YearSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jms.core.JmsTemplate;

public class IntegrationSteps {

  @Autowired
  private JmsTemplate jmsTemplate;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CucumberIntegrationContext context;

  @Value("${application.messaging.queue.update-trainer-workload}")
  private String updateTrainerWorkloadQueue;

  @Given("a list of trainer records to update their workload:")
  public void a_list_of_trainer_records_to_update_their_workload(String json) throws IOException {
    TrainerWorkloadUpdateDtoList workloadUpdateDtoList = objectMapper.readValue(json,
        TrainerWorkloadUpdateDtoList.class);

    List<TrainerWorkload> expectedWorkloadList = buildExpectedWorkloadList(workloadUpdateDtoList);

    context.setWorkloadUpdateDtoList(workloadUpdateDtoList);
    context.setExpectedWorkloadList(expectedWorkloadList);
  }

  @When("a message is sent to update trainer workload")
  public void a_message_is_sent_to_update_trainer_workload() throws InterruptedException {
    jmsTemplate.convertAndSend(updateTrainerWorkloadQueue, context.getWorkloadUpdateDtoList());
    Thread.sleep(10000);
  }

  @Then("the trainer records should be successfully updated")
  public void the_trainer_records_should_be_successfully_updated() {
    for (TrainerWorkload expectedWorkload : context.getExpectedWorkloadList()) {
      TrainerWorkload trainerWorkload = mongoTemplate.findById(expectedWorkload.getUsername(),
          TrainerWorkload.class);

      if (expectedWorkload.getYears() == null || expectedWorkload.getYears().isEmpty()) {
        assertNull(trainerWorkload);
      } else {
        assertNotNull(trainerWorkload);
        assertEquals(expectedWorkload.getUsername(), trainerWorkload.getUsername());
        assertEquals(expectedWorkload.getYears(), trainerWorkload.getYears());
      }
    }
  }

  private List<TrainerWorkload> buildExpectedWorkloadList(
      TrainerWorkloadUpdateDtoList workloadUpdateDtoList) {
    List<TrainerWorkload> expectedWorkloadList = new ArrayList<>();

    for (TrainerWorkloadUpdateDto updateDto : workloadUpdateDtoList.getItems()) {
      TrainerWorkload trainerWorkload = fetchOrCreateTrainerWorkload(updateDto.getUsername());

      int year = updateDto.getTrainingDate().getYear();
      int month = updateDto.getTrainingDate().getMonth().getValue();

      TrainerWorkload expectedWorkload = getExpectedWorkload(expectedWorkloadList, trainerWorkload,
          updateDto);
      if (updateDto.getActionType().equals(ActionType.ADD)) {
        handleAddAction(expectedWorkload, year, month, updateDto.getTrainingDuration());
      } else if (updateDto.getActionType().equals(ActionType.DELETE)) {
        handleDeleteAction(expectedWorkload, year, month, updateDto.getTrainingDuration());
      }
    }

    context.setExpectedWorkloadList(expectedWorkloadList);
    return expectedWorkloadList;
  }

  private TrainerWorkload fetchOrCreateTrainerWorkload(String username) {
    return Optional.ofNullable(mongoTemplate.findById(username, TrainerWorkload.class))
        .orElseGet(() -> TrainerWorkload.builder().username(username).build());
  }

  private void handleAddAction(TrainerWorkload expectedWorkload, int year, int month,
      long trainingDuration) {
    expectedWorkload.getYears().stream()
        .filter(yearSummary -> yearSummary.getYear() == year)
        .findFirst()
        .ifPresentOrElse(
            yearSummary -> updateOrAddMonthSummary(yearSummary, month, trainingDuration),
            () -> addNewYearSummary(expectedWorkload, year, month, trainingDuration)
        );
  }

  private void handleDeleteAction(TrainerWorkload expectedWorkload, int year, int month,
      long trainingDuration) {
    expectedWorkload.getYears().stream()
        .filter(yearSummary -> yearSummary.getYear() == year)
        .findFirst()
        .ifPresent(yearSummary -> updateOrRemoveMonthSummary(expectedWorkload, yearSummary, month,
            trainingDuration));
  }

  private void updateOrAddMonthSummary(YearSummary yearSummary, int month, long trainingDuration) {
    yearSummary.getMonths().stream()
        .filter(monthSummary -> monthSummary.getMonth() == month)
        .findFirst()
        .ifPresentOrElse(
            monthSummary -> monthSummary.setDuration(monthSummary.getDuration() + trainingDuration),
            () -> yearSummary.getMonths().add(new MonthSummary(month, trainingDuration))
        );
  }

  private void addNewYearSummary(TrainerWorkload expectedWorkload, int year, int month,
      long trainingDuration) {
    YearSummary newYearSummary = new YearSummary(year, new ArrayList<>());
    newYearSummary.getMonths().add(new MonthSummary(month, trainingDuration));
    expectedWorkload.getYears().add(newYearSummary);
  }

  private void updateOrRemoveMonthSummary(TrainerWorkload expectedWorkload, YearSummary yearSummary,
      int month, long trainingDuration) {
    yearSummary.getMonths().stream()
        .filter(monthSummary -> monthSummary.getMonth() == month)
        .findFirst()
        .ifPresent(monthSummary -> {
          long result = monthSummary.getDuration() - trainingDuration;
          if (result > 0) {
            monthSummary.setDuration(result);
          } else {
            yearSummary.getMonths().remove(monthSummary);
            if (yearSummary.getMonths().isEmpty()) {
              expectedWorkload.getYears().remove(yearSummary);
            }
          }
        });
  }

  private TrainerWorkload getExpectedWorkload(List<TrainerWorkload> expectedWorkloadList,
      TrainerWorkload fetchedWorkload, TrainerWorkloadUpdateDto workloadUpdateDto) {
    return expectedWorkloadList.stream()
        .filter(workload -> workload.getUsername().equals(workloadUpdateDto.getUsername()))
        .findFirst()
        .orElseGet(() -> {
          expectedWorkloadList.add(fetchedWorkload);
          return fetchedWorkload;
        });
  }
}
