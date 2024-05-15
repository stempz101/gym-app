package com.epam.gymapp.reportsmicroservice.consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

@SpringBootTest
@Import(TestJmsConfiguration.class)
public class TrainerWorkloadConsumerTest {

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
    when(trainerWorkloadService
        .retrieveTrainersWorkloadForMonth(year, month, null, null))
        .thenReturn(expectedResult);

    sendMessageToRetrieveTrainerWorkload(year, month, null, null, correlationID);
    TrainerWorkloadDtoList result = receiveMessageWithTrainerWorkload(correlationID);

    // Then
    verify(trainerWorkloadConsumer, times(1))
        .retrieveTrainersWorkloadForMonth(year, month, null, null);
    verify(trainerWorkloadService, times(1))
        .retrieveTrainersWorkloadForMonth(year, month, null, null);

    assertThat(result, notNullValue());
    assertThat(result.getItems(), hasSize(expectedResult.getItems().size()));
    assertThat(result.getItems(), hasItems(trainerWorkloadDto1, trainerWorkloadDto2));
  }

  @Test
  void retrieveTrainersWorkloadForMonth_WithFirstNameAndLastName_Success() {
    // Given
    int year = 2024;
    int month = 4;
    String firstName = TrainerWorkloadTestUtil.TEST_TRAINER_FIRST_NAME_1;
    String lastName = TrainerWorkloadTestUtil.TEST_TRAINER_LAST_NAME_1;
    String correlationID = UUID.randomUUID().toString();
    TrainerWorkloadDto trainerWorkloadDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadDto1(2024, 4, 10);
    TrainerWorkloadDtoList expectedResult = new TrainerWorkloadDtoList(Collections.singletonList(trainerWorkloadDto1));

    // When
    when(trainerWorkloadService
        .retrieveTrainersWorkloadForMonth(year, month, firstName, lastName))
        .thenReturn(expectedResult);

    sendMessageToRetrieveTrainerWorkload(year, month, firstName, lastName, correlationID);
    TrainerWorkloadDtoList result = receiveMessageWithTrainerWorkload(correlationID);

    // Then
    verify(trainerWorkloadConsumer, times(1))
        .retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), anyString(), anyString());
    verify(trainerWorkloadService, times(1))
        .retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), anyString(), anyString());

    assertThat(result, notNullValue());
    assertThat(result.getItems(), hasSize(expectedResult.getItems().size()));
    assertThat(result.getItems(), hasItems(trainerWorkloadDto1));
  }

  private void sendMessageToRetrieveTrainerWorkload(
      int year, int month, String firstName, String lastName, String correlationID) {
    jmsTemplate.send(retrieveTrainerWorkloadRequestQueue, session -> {
      Message message = session.createMessage();
      message.setIntProperty("year", year);
      message.setIntProperty("month", month);
      message.setStringProperty("firstName", firstName);
      message.setStringProperty("lastName", lastName);
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
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto1(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto1(2025, 5, 60, ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2));

    // When
    doNothing().when(trainerWorkloadService).updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    sendMessageToUpdateTrainerWorkload(trainerWorkloadUpdateDtoList);

    Thread.sleep(5000);

    // Then
    verify(trainerWorkloadConsumer, times(1))
        .updateTrainersWorkload(any(TrainerWorkloadUpdateDtoList.class));
    verify(trainerWorkloadService, times(1))
        .updateTrainersWorkload(any(TrainerWorkloadUpdateDtoList.class));
  }

  @Test
  void updateTrainersRecords_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        new TrainerWorkloadUpdateDto(null, null, null, null, null, null, null);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        new TrainerWorkloadUpdateDto(null, null, null, null, null, null, null);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2));

    // When
    sendMessageToUpdateTrainerWorkload(trainerWorkloadUpdateDtoList);

    Thread.sleep(5000);

    // Then
    verifyNoInteractions(trainerWorkloadConsumer);
    verifyNoInteractions(trainerWorkloadService);
  }

  private void sendMessageToUpdateTrainerWorkload(TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList) {
    jmsTemplate.convertAndSend(updateTrainerWorkloadQueue, trainerWorkloadUpdateDtoList);
  }
}
