package com.epam.gymapp.reportsmicroservice.consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.reportsmicroservice.config.TestJmsConfiguration;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.service.TrainerWorkloadService;
import com.epam.gymapp.reportsmicroservice.test.utils.TrainerWorkloadTestUtil;
import jakarta.jms.Message;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest
@Import(TestJmsConfiguration.class)
public class TrainerWorkloadConsumerTest {

  static GenericContainer<?> activeMQ = new GenericContainer<>("rmohr/activemq:latest")
      .withExposedPorts(61616);

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    activeMQ.start();
    registry.add("spring.activemq.broker-url",
        () -> "tcp://localhost:" + activeMQ.getMappedPort(61616));
    registry.add("spring.jta.enabled", () -> "false");
  }

  @SpyBean
  private TrainerWorkloadConsumer trainerWorkloadConsumer;

  @MockBean
  private TrainerWorkloadService trainerWorkloadService;

  @Autowired
  private JmsTemplate jmsTemplate;

  @Value("${application.messaging.queue.retrieve-trainer-workload.request}")
  private String retrieveTrainerWorkloadRequestQueue;

  @Value("${application.messaging.queue.retrieve-trainer-workload.response}")
  private String retrieveTrainerWorkloadResponseQueue;

  @Value("${application.messaging.queue.update-trainer-workload}")
  private String updateTrainerWorkloadQueue;

  @Test
  void retrieveTrainersWorkloadForMonth_Success() {
    // Given
    int year = 2024;
    int month = 4;
    String correlationID = UUID.randomUUID().toString();
    TrainerWorkloadDto trainerWorkloadDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadDto1(2024, 4, 10);
    TrainerWorkloadDto trainerWorkloadDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadDto2(2024, 4, 60);
    TrainerWorkloadDtoList expectedResult = new TrainerWorkloadDtoList(List.of(trainerWorkloadDto1, trainerWorkloadDto2));

    // When
    when(trainerWorkloadService.retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any()))
        .thenReturn(expectedResult);

    sendMessageToRetrieveTrainerWorkload(year, month, null, correlationID);
    TrainerWorkloadDtoList result = receiveMessageWithTrainerWorkload(correlationID);

    // Then
    verify(trainerWorkloadConsumer, times(1))
        .retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any());
    verify(trainerWorkloadService, times(1))
        .retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any());

    assertThat(result, notNullValue());
    assertThat(result.getItems(), hasSize(expectedResult.getItems().size()));
    assertThat(result.getItems(), hasItems(trainerWorkloadDto1, trainerWorkloadDto2));
  }

  @Test
  void retrieveTrainersWorkloadForMonth_WithUsername_Success() {
    // Given
    int year = 2024;
    int month = 4;
    String username = TrainerWorkloadTestUtil.TEST_TRAINER_USERNAME_1;
    String correlationID = UUID.randomUUID().toString();
    TrainerWorkloadDto trainerWorkloadDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadDto1(2024, 4, 10);
    TrainerWorkloadDtoList expectedResult = new TrainerWorkloadDtoList(Collections.singletonList(trainerWorkloadDto1));

    // When
    when(trainerWorkloadService.retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any()))
        .thenReturn(expectedResult);

    sendMessageToRetrieveTrainerWorkload(year, month, username, correlationID);
    TrainerWorkloadDtoList result = receiveMessageWithTrainerWorkload(correlationID);

    // Then
    verify(trainerWorkloadConsumer, times(1))
        .retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any());
    verify(trainerWorkloadService, times(1))
        .retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any());

    assertThat(result, notNullValue());
    assertThat(result.getItems(), hasSize(expectedResult.getItems().size()));
    assertThat(result.getItems(), hasItems(trainerWorkloadDto1));
  }

  private void sendMessageToRetrieveTrainerWorkload(
      int year, int month, String username, String correlationID) {
    jmsTemplate.send(retrieveTrainerWorkloadRequestQueue, session -> {
      Message message = session.createMessage();
      message.setIntProperty("year", year);
      message.setIntProperty("month", month);
      message.setStringProperty("username", username);
      message.setJMSCorrelationID(correlationID);
      return message;
    });
  }

  private TrainerWorkloadDtoList receiveMessageWithTrainerWorkload(String correlationID) {
    return (TrainerWorkloadDtoList) jmsTemplate
        .receiveSelectedAndConvert(retrieveTrainerWorkloadResponseQueue,
            String.format("JMSCorrelationID = '%s'", correlationID));
  }

  @Test
  void updateTrainersRecords_Success() throws InterruptedException {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2));

    // When
    doNothing().when(trainerWorkloadService).updateTrainersWorkload(any());

    sendMessageToUpdateTrainerWorkload(trainerWorkloadUpdateDtoList);

    Thread.sleep(5000);

    // Then
    verify(trainerWorkloadConsumer, times(1)).updateTrainersWorkload(any());
    verify(trainerWorkloadService, times(1)).updateTrainersWorkload(any());
  }

  @Test
  void updateTrainersRecords_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, null);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, null);
    trainerWorkloadUpdateDto1.setUsername(null);
    trainerWorkloadUpdateDto1.setFirstName(null);
    trainerWorkloadUpdateDto1.setLastName(null);
    trainerWorkloadUpdateDto1.setIsActive(null);
    trainerWorkloadUpdateDto2.setIsActive(null);
    trainerWorkloadUpdateDto2.setTrainingDate(null);
    trainerWorkloadUpdateDto2.setTrainingDuration(null);
    trainerWorkloadUpdateDto2.setActionType(null);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2));

    // When
    sendMessageToUpdateTrainerWorkload(trainerWorkloadUpdateDtoList);

    Thread.sleep(5000);

    // Then
    verify(trainerWorkloadConsumer, times(0)).updateTrainersWorkload(any());
    verify(trainerWorkloadService, times(0)).updateTrainersWorkload(any());
  }

  private void sendMessageToUpdateTrainerWorkload(TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList) {
    jmsTemplate.convertAndSend(updateTrainerWorkloadQueue, trainerWorkloadUpdateDtoList);
  }
}
