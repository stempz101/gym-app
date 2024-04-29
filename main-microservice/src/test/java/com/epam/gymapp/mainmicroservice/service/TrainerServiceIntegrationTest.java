package com.epam.gymapp.mainmicroservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.gymapp.mainmicroservice.exception.TrainerWorkingHoursUpdateException;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.mainmicroservice.test.utils.TrainerTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainingTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.UserTestUtil;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest
public class TrainerServiceIntegrationTest {

  static GenericContainer<?> activeMQ = new GenericContainer<>("rmohr/activemq:latest")
      .withExposedPorts(61616);

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    activeMQ.start();
    registry.add("spring.activemq.broker-url",
        () -> "tcp://localhost:" + activeMQ.getMappedPort(61616));
  }

  @Autowired
  private TrainerService trainerService;

  @SpyBean
  private JmsTemplate jmsTemplate;

  @Autowired
  private CircuitBreakerRegistry circuitBreakerRegistry;

  @Value("${application.messaging.queue.retrieve-trainer-workload.response}")
  private String retrieveTrainerWorkloadResponseQueue;

  @BeforeEach
  void setUp() {
    circuitBreakerRegistry.circuitBreaker("reportsMicroserviceCircuitBreaker").reset();
  }

  @Test
  void retrieveTrainersWorkloadForMonth_Success() {
    // Given
    int year = 2024;
    int month = 4;
    TrainerWorkloadDto trainerWorkloadDto1 = TrainerTestUtil
        .getTrainerWorkloadDto1(2024, 4, 120);
    TrainerWorkloadDto trainerWorkloadDto2 = TrainerTestUtil
        .getTrainerWorkloadDto2(2024, 4, 340);
    UUID correlationId = UUID.randomUUID();
    List<TrainerWorkloadDto> expectedResult = List.of(trainerWorkloadDto1, trainerWorkloadDto2);

    try (MockedStatic<UUID> uuidUtils = mockStatic(UUID.class)) {
      // When
      uuidUtils.when(UUID::randomUUID).thenReturn(correlationId);

      jmsTemplate.convertAndSend(retrieveTrainerWorkloadResponseQueue,
          new TrainerWorkloadDtoList(expectedResult), message -> {
            message.setJMSCorrelationID(correlationId.toString());
            return message;
          });

      List<TrainerWorkloadDto> result = trainerService
          .retrieveTrainersWorkloadForMonth(year, month, null);

      // Then
      assertThat(result, hasSize(expectedResult.size()));
      assertThat(result, hasItems(trainerWorkloadDto1, trainerWorkloadDto2));
    }
  }

  @Test
  void retrieveTrainersWorkloadForMonth_WithUsername_Success() {
    // Given
    int year = 2024;
    int month = 4;
    String username = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    TrainerWorkloadDto trainerWorkloadDto1 = TrainerTestUtil
        .getTrainerWorkloadDto1(2024, 4, 120);
    UUID correlationId = UUID.randomUUID();
    List<TrainerWorkloadDto> expectedResult = Collections.singletonList(trainerWorkloadDto1);

    try (MockedStatic<UUID> uuidUtils = mockStatic(UUID.class)) {
      // When
      uuidUtils.when(UUID::randomUUID).thenReturn(correlationId);

      jmsTemplate.convertAndSend(retrieveTrainerWorkloadResponseQueue,
          new TrainerWorkloadDtoList(expectedResult), message -> {
            message.setJMSCorrelationID(correlationId.toString());
            return message;
          });

      List<TrainerWorkloadDto> result = trainerService
          .retrieveTrainersWorkloadForMonth(year, month, username);

      // Then
      assertThat(result, hasSize(expectedResult.size()));
      assertThat(result, hasItems(trainerWorkloadDto1));
    }
  }

  @Test
  void retrieveTrainersWorkloadForMonth_NoResponse_Success() {
    // Given
    int year = 2024;
    int month = 4;
    TrainerWorkloadDto fallbackObject = TrainerWorkloadDto.getFallbackObject(year, month, null);
    UUID correlationId = UUID.randomUUID();
    List<TrainerWorkloadDto> expectedResult = Collections.singletonList(fallbackObject);

    try (MockedStatic<UUID> uuidUtils = mockStatic(UUID.class)) {
      // When
      uuidUtils.when(UUID::randomUUID).thenReturn(correlationId);
      doReturn(null).when(jmsTemplate).receiveSelectedAndConvert(anyString(), anyString());

      List<TrainerWorkloadDto> result = trainerService
          .retrieveTrainersWorkloadForMonth(year, month, null);

      // Then
      assertThat(result, hasSize(expectedResult.size()));
      assertThat(result, hasItems(fallbackObject));
    }
  }

  @Test
  void retrieveTrainersWorkloadForMonth_RetryAndCircuitBreaker() {
    // Given
    int year = 2024;
    int month = 4;
    String username = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    TrainerWorkloadDto fallbackObject = TrainerWorkloadDto.getFallbackObject(year, month, username);
    List<TrainerWorkloadDto> expectedResult = Collections.singletonList(fallbackObject);

    // When
    doThrow(RuntimeException.class).when(jmsTemplate).send(anyString(), any());

    for (int i = 0; i < 3; i++) {
      List<TrainerWorkloadDto> result = trainerService
          .retrieveTrainersWorkloadForMonth(year, month, username);

      // Then
      assertThat(result, hasSize(expectedResult.size()));
      assertThat(result, hasItems(fallbackObject));

      CircuitBreaker circuitBreaker = circuitBreakerRegistry
          .circuitBreaker("reportsMicroserviceCircuitBreaker");
      if (i < 2) {
        assertSame(State.CLOSED, circuitBreaker.getState());
      } else {
        assertSame(State.OPEN, circuitBreaker.getState());
      }
    }
  }


  @Test
  void updateTrainerWorkload_WithOneUpdate_Success() {
    // Given
    Training training = TrainingTestUtil.getTraining1();

    // When
    trainerService.updateTrainerWorkload(training, ActionType.ADD);

    // Then
    verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(), any());
  }

  @Test
  void updateTrainerWorkload_WithOneUpdate_RetryAndCircuitBreaker() {
    // Given
    Training training = TrainingTestUtil.getTraining1();

    // When
    doThrow(RuntimeException.class).when(jmsTemplate).convertAndSend(anyString(), any(), any());

    // Then
    for (int i = 0; i < 3; i++) {
      assertThrows(TrainerWorkingHoursUpdateException.class,
          () -> trainerService.updateTrainerWorkload(training, ActionType.ADD));

      CircuitBreaker circuitBreaker = circuitBreakerRegistry
          .circuitBreaker("reportsMicroserviceCircuitBreaker");
      if (i < 2) {
        assertSame(State.CLOSED, circuitBreaker.getState());
      } else {
        assertSame(State.OPEN, circuitBreaker.getState());
      }
    }
  }

  @Test
  void updateTrainerWorkload_WithListOfUpdates_Success() {
    // Given
    List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos =
        Collections.singletonList(TrainerTestUtil.getTrainerWorkloadUpdateDto1(ActionType.ADD));

    // When
    trainerService.updateTrainerWorkload(trainerWorkloadUpdateDtos);

    // Then
    verify(jmsTemplate, times(1)).convertAndSend(anyString(), any(), any());
  }

  @Test
  void updateTrainerWorkload_WithListOfUpdates_RetryAndCircuitBreaker() {
    // Given
    List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos =
        Collections.singletonList(TrainerTestUtil.getTrainerWorkloadUpdateDto1(ActionType.ADD));

    // When
    doThrow(RuntimeException.class).when(jmsTemplate).convertAndSend(anyString(), any(), any());

    // Then
    for (int i = 0; i < 3; i++) {
      assertThrows(TrainerWorkingHoursUpdateException.class,
          () -> trainerService.updateTrainerWorkload(trainerWorkloadUpdateDtos));

      CircuitBreaker circuitBreaker = circuitBreakerRegistry
          .circuitBreaker("reportsMicroserviceCircuitBreaker");
      if (i < 2) {
        assertSame(State.CLOSED, circuitBreaker.getState());
      } else {
        assertSame(State.OPEN, circuitBreaker.getState());
      }
    }
  }
}
