package com.epam.gymapp.reportsmicroservice.bdd.integration.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.gymapp.reportsmicroservice.bdd.integration.context.CucumberIntegrationContext;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.repository.TrainerWorkloadRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.jms.Message;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
  private TrainerWorkloadRepository trainerWorkloadRepository;

  @Autowired
  private CucumberIntegrationContext context;

  @Value("${application.messaging.queue.retrieve-trainer-workload.request}")
  private String retrieveTrainerWorkloadRequestQueue;

  @Value("${application.messaging.queue.retrieve-trainer-workload.response}")
  private String retrieveTrainerWorkloadResponseQueue;

  @Value("${application.messaging.queue.update-trainer-workload}")
  private String updateTrainerWorkloadQueue;

  @Given("fields to retrieve trainer workload as below:")
  public void fields_to_retrieve_trainer_workload_as_below(Map<String, String> map) {
    context.setWorkloadYear(Integer.parseInt(map.get("year")));
    context.setWorkloadMonth(map.get("month"));
    context.setTrainerFirstName(map.get("firstName"));
    context.setTrainerLastName(map.get("lastName"));

    context.setCorrelationId(UUID.randomUUID());
  }

  @Given("a list of trainer records to update their workload:")
  public void a_list_of_trainer_records_to_update_their_workload(String json) throws IOException {
    TrainerWorkloadUpdateDtoList workloadUpdateDtoList = objectMapper.readValue(json,
        TrainerWorkloadUpdateDtoList.class);

    List<TrainerWorkload> expectedWorkloadList = buildExpectedWorkloadList(workloadUpdateDtoList);

    context.setWorkloadUpdateDtoList(workloadUpdateDtoList);
    context.setExpectedWorkloadList(expectedWorkloadList);
  }

  @When("a message is sent to retrieve trainer workload")
  public void a_message_is_sent_to_retrieve_trainer_workload() {
    jmsTemplate.send(retrieveTrainerWorkloadRequestQueue, session -> {
      Message message = session.createMessage();
      message.setIntProperty("year", context.getWorkloadYear());
      message.setIntProperty("month", context.getWorkloadMonth().getValue());
      message.setStringProperty("firstName", context.getTrainerFirstName());
      message.setStringProperty("lastName", context.getTrainerLastName());
      message.setJMSCorrelationID(context.getCorrelationId().toString());
      return message;
    });
  }

  @When("a message is sent to update trainer workload")
  public void a_message_is_sent_to_update_trainer_workload() throws InterruptedException {
    jmsTemplate.convertAndSend(updateTrainerWorkloadQueue, context.getWorkloadUpdateDtoList());
    Thread.sleep(10000);
  }

  @Then("a response message should be sent with retrieved trainer workload")
  public void a_response_message_should_be_sent_with_retrieved_trainer_workload() {
    TrainerWorkloadDtoList trainerWorkloadDtoList = (TrainerWorkloadDtoList) jmsTemplate
        .receiveSelectedAndConvert(retrieveTrainerWorkloadResponseQueue,
            String.format("JMSCorrelationID = '%s'", context.getCorrelationId().toString()));
    assertNotNull(trainerWorkloadDtoList);

    List<TrainerWorkloadDto> workloadDtoList = trainerWorkloadDtoList.getItems();
    assertNotNull(workloadDtoList);

    workloadDtoList.forEach(workloadDto -> {
      assertEquals(context.getWorkloadYear(), workloadDto.getYear());
      assertEquals(context.getWorkloadMonth(), workloadDto.getMonth());
      if (context.getTrainerFirstName() != null) {
        assertEquals(context.getTrainerFirstName(), workloadDto.getFirstName());
      }
      if (context.getTrainerLastName() != null) {
        assertEquals(context.getTrainerLastName(), workloadDto.getLastName());
      }
    });
  }

  @Then("the trainer records should be successfully updated")
  public void the_trainer_records_should_be_successfully_updated() {
    for (TrainerWorkload expectedWorkload : context.getExpectedWorkloadList()) {
      TrainerWorkload trainerWorkload = mongoTemplate.findById(expectedWorkload.getUsername(), TrainerWorkload.class);

      if (expectedWorkload.getYears() == null || expectedWorkload.getYears().isEmpty()) {
        assertNull(trainerWorkload);
      } else {
        assertNotNull(trainerWorkload);
        assertEquals(expectedWorkload.getUsername(), trainerWorkload.getUsername());
        assertEquals(expectedWorkload.getYears(), trainerWorkload.getYears());
      }
    }
  }

  @Then("the trainer record should be successfully deleted")
  public void the_trainer_record_should_be_successfully_deleted() throws InterruptedException {
    Thread.sleep(10000);
    verify(trainerWorkloadRepository, times(1)).deleteById(anyString());
  }

  private List<TrainerWorkload> buildExpectedWorkloadList(TrainerWorkloadUpdateDtoList workloadUpdateDtoList) {
    List<TrainerWorkload> expectedWorkloadList = new ArrayList<>();

    for (TrainerWorkloadUpdateDto updateDto : workloadUpdateDtoList.getItems()) {
      TrainerWorkload trainerWorkload = Optional.ofNullable(mongoTemplate
          .findById(updateDto.getUsername(), TrainerWorkload.class))
          .orElseGet(() -> TrainerWorkload.builder().username(updateDto.getUsername()).build());

      int year = updateDto.getTrainingDate().getYear();
      Month month = updateDto.getTrainingDate().getMonth();

      if (updateDto.getActionType().equals(ActionType.ADD)) {
        TrainerWorkload expectedWorkload = getExpectedWorkload(expectedWorkloadList,
            trainerWorkload, updateDto);

        Map<Month, Long> months = expectedWorkload.getYears().computeIfAbsent(year, key -> new HashMap<>());
        months.put(month, months.computeIfAbsent(month, key -> 0L) + updateDto.getTrainingDuration());
      } else if (updateDto.getActionType().equals(ActionType.DELETE)) {
        if (trainerWorkload != null) {
          TrainerWorkload expectedWorkload = getExpectedWorkload(expectedWorkloadList,
              trainerWorkload, updateDto);

          expectedWorkload.getYears().computeIfPresent(year, (yearKey, months) -> {
            months.computeIfPresent(month, (monthKey, workloadDuration) -> {
              long updatedTrainingDuration = workloadDuration - updateDto.getTrainingDuration();
              return updatedTrainingDuration > 0 ? updatedTrainingDuration : null;
            });

            return months.isEmpty() ? null : months;
          });
        }
      }
    }

    return expectedWorkloadList;
  }

  private TrainerWorkload getExpectedWorkload(List<TrainerWorkload> expectedWorkloadList,
      TrainerWorkload fetchedWorkload, TrainerWorkloadUpdateDto workloadUpdateDto) {
    Optional<TrainerWorkload> expectedWorkloadOpt = expectedWorkloadList.stream()
        .filter(workload -> workload.getUsername().equals(workloadUpdateDto.getUsername()))
        .findFirst();

    return expectedWorkloadOpt.orElseGet(() -> {
      expectedWorkloadList.add(fetchedWorkload);
      return fetchedWorkload;
    });
  }
}
