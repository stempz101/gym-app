package com.epam.gymapp.mainmicroservice.producer.jms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.gymapp.mainmicroservice.config.TestJmsConfiguration;
import com.epam.gymapp.mainmicroservice.exception.TrainerWorkloadUpdateException;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.mainmicroservice.test.utils.TrainerTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainingTestUtil;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

@SpringBootTest
@Import(TestJmsConfiguration.class)
public class JMSReportsProducerTest {

  @Autowired
  private JMSReportsProducer reportsProducer;

  @SpyBean
  private JmsTemplate jmsTemplate;

  @Autowired
  private CircuitBreakerRegistry circuitBreakerRegistry;

  @BeforeEach
  void setUp() {
    circuitBreakerRegistry.circuitBreaker("reportsMicroserviceCircuitBreaker").reset();
  }

  @Test
  void updateTrainerWorkload_WithOneUpdate_Success() {
    // Given
    Training training = TrainingTestUtil.getTraining1();

    // When
    reportsProducer.updateTrainerWorkload(training, ActionType.ADD);

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
      assertThrows(TrainerWorkloadUpdateException.class,
          () -> reportsProducer.updateTrainerWorkload(training, ActionType.ADD));

      CircuitBreaker circuitBreaker = circuitBreakerRegistry
          .circuitBreaker("reportsMicroserviceCircuitBreaker");
      if (i < 2) {
        assertEquals(State.CLOSED, circuitBreaker.getState());
      } else {
        assertEquals(State.OPEN, circuitBreaker.getState());
      }
    }
  }

  @Test
  void updateTrainerWorkload_WithListOfUpdates_Success() {
    // Given
    List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos =
        Collections.singletonList(TrainerTestUtil.getTrainerWorkloadUpdateDto1(ActionType.ADD));

    // When
    reportsProducer.updateTrainerWorkload(trainerWorkloadUpdateDtos);

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
      assertThrows(TrainerWorkloadUpdateException.class,
          () -> reportsProducer.updateTrainerWorkload(trainerWorkloadUpdateDtos));

      CircuitBreaker circuitBreaker = circuitBreakerRegistry
          .circuitBreaker("reportsMicroserviceCircuitBreaker");
      if (i < 2) {
        assertEquals(State.CLOSED, circuitBreaker.getState());
      } else {
        assertEquals(State.OPEN, circuitBreaker.getState());
      }
    }
  }
}
