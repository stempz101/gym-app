package com.epam.gymapp.mainmicroservice.bdd.component.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import com.epam.gymapp.mainmicroservice.bdd.component.context.CucumberComponentContext;
import com.epam.gymapp.mainmicroservice.dto.TrainerInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.service.TrainerService;
import com.epam.gymapp.mainmicroservice.test.utils.TrainerTestUtil;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.UUID;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;

public class TrainerServiceSteps {

  @Autowired
  private TrainerService trainerService;

  @Autowired
  private JmsTemplate jmsTemplate;

  @Autowired
  private CucumberComponentContext context;

  @Value("${application.messaging.queue.retrieve-trainer-workload.response}")
  private String retrieveTrainerWorkloadResponseQueue;

  private TrainerUpdateDto trainerUpdateDto;
  private TrainerInfoDto trainerInfo;
  private List<TrainerInfoDto> trainerInfoList;
  private List<TrainerShortInfoDto> trainerShortInfoList;
  private List<TrainingInfoDto> trainingInfoList;
  private List<TrainerWorkloadDto> expectedTrainerWorkloadList;
  private List<TrainerWorkloadDto> resultTrainerWorkloadList;
  private TrainerWorkloadDto trainerWorkload;

  private UUID correlationId;

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

  @Given("a trainer with username {string} has {long} working hours for the month {string} in {int}")
  public void a_trainer_with_username_has_working_hours_for_the_month_in(
      String username, Long trainerWorkingHours, String workloadMonth, Integer workloadYear) {
    TrainerInfoDto trainerInfoDto = trainerService.selectTrainer(username);

    trainerWorkload = TrainerWorkloadDto.builder()
        .username(username)
        .firstName(trainerInfoDto.getFirstName())
        .lastName(trainerInfoDto.getLastName())
        .isActive(trainerInfoDto.isActive())
        .year(workloadYear)
        .month(Month.valueOf(workloadMonth))
        .duration(trainerWorkingHours)
        .build();

    expectedTrainerWorkloadList = List.of(trainerWorkload);
  }

  @Given("there are trainers with workload for the month {string} in {int}")
  public void there_are_trainers_with_workload_for_the_month_in(
      String workloadMonth, Integer workloadYear) {
    int month = Month.valueOf(workloadMonth).getValue();
    TrainerWorkloadDto trainerWorkloadDto1 = TrainerTestUtil
        .getTrainerWorkloadDto1(workloadYear, month, 120);
    TrainerWorkloadDto trainerWorkloadDto2 = TrainerTestUtil
        .getTrainerWorkloadDto2(workloadYear, month, 340);

    expectedTrainerWorkloadList = List.of(trainerWorkloadDto1, trainerWorkloadDto2);
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

  @When("a request to retrieve workload by name {string} {string} for {string} {int} is made")
  public void a_request_to_retrieve_s_workload_for_for_is_made(
      String firstName, String lastName, String workloadMonth, Integer workloadYear) {
    UUID correlationId = UUID.randomUUID();

    try (MockedStatic<UUID> uuidUtils = mockStatic(UUID.class)) {
      uuidUtils.when(UUID::randomUUID).thenReturn(correlationId);

      sendWorkloadResultToResponseQueue(expectedTrainerWorkloadList, correlationId);

      resultTrainerWorkloadList = trainerService.retrieveTrainersWorkloadForMonth(
          workloadYear, Month.valueOf(workloadMonth).getValue(), firstName, lastName);
    }
  }

  @When("a request to retrieve workload for all trainers for {string} {int} is made")
  public void a_request_to_retrieve_workload_for_all_trainers_for_is_made(
      String workloadMonth, Integer workloadYear) {
    UUID correlationId = UUID.randomUUID();

    try (MockedStatic<UUID> uuidUtils = mockStatic(UUID.class)) {
      uuidUtils.when(UUID::randomUUID).thenReturn(correlationId);

      sendWorkloadResultToResponseQueue(expectedTrainerWorkloadList, correlationId);

      resultTrainerWorkloadList = trainerService.retrieveTrainersWorkloadForMonth(
          workloadYear, Month.valueOf(workloadMonth).getValue(), null, null);
    }
  }

  @When("a request to retrieve workload for all trainers for {string} {int} is made with no response after")
  public void a_request_to_retrieve_workload_for_all_trainers_for_is_made_with_no_response_after(
      String workloadMonth, Integer workloadYear) {
    resultTrainerWorkloadList = trainerService.retrieveTrainersWorkloadForMonth(
        workloadYear, Month.valueOf(workloadMonth).getValue(), null, null);
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

  @Then("the correct workload data is retrieved")
  public void the_correct_workload_data_is_retrieved() {
    assertEquals(expectedTrainerWorkloadList, resultTrainerWorkloadList);
  }

  @Then("the fallback workload data response is retrieved")
  public void the_fallback_workload_data_response_is_retrieved() {
    TrainerWorkloadDto trainerWorkloadDto = expectedTrainerWorkloadList.get(0);

    int workloadYear = trainerWorkloadDto.getYear();
    int workloadMonth = trainerWorkloadDto.getMonth().getValue();

    TrainerWorkloadDto fallbackObject = TrainerWorkloadDto.getFallbackObject(
        workloadYear, workloadMonth, null, null);
    List<TrainerWorkloadDto> workloadListWithFallbackResult = List.of(fallbackObject);

    assertEquals(workloadListWithFallbackResult, resultTrainerWorkloadList);
  }

  private void sendWorkloadResultToResponseQueue(
      List<TrainerWorkloadDto> trainerWorkloadList, UUID correlationId) {
    jmsTemplate.convertAndSend(retrieveTrainerWorkloadResponseQueue,
        new TrainerWorkloadDtoList(expectedTrainerWorkloadList),
        message -> {
          message.setJMSCorrelationID(correlationId.toString());
          return message;
        });
  }
}
