package com.epam.gymapp.mainmicroservice.producer;

import com.epam.gymapp.mainmicroservice.exception.TrainerWorkloadUpdateException;
import com.epam.gymapp.mainmicroservice.mapper.TrainerMapper;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.jms.Message;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportsProducer {

  private static final Logger log = LoggerFactory.getLogger(ReportsProducer.class);

  private final TrainerMapper trainerMapper;
  private final JmsTemplate jmsTemplate;

  @Value("${application.logging.transaction-id.key}")
  private String transactionIdKey;

  @Value("${application.logging.transaction-id.header}")
  private String transactionIdHeader;

  @Value("${application.messaging.queue.retrieve-trainer-workload.request}")
  private String retrieveTrainerWorkloadRequestQueue;

  @Value("${application.messaging.queue.retrieve-trainer-workload.response}")
  private String retrieveTrainerWorkloadResponseQueue;

  @Value("${application.messaging.queue.update-trainer-workload}")
  private String updateTrainerWorkloadQueue;

  @Retry(name = "reportsMicroserviceRetry")
  @CircuitBreaker(name = "reportsMicroserviceCircuitBreaker",
      fallbackMethod = "retrieveTrainersWorkloadForMonthFailure")
  public List<TrainerWorkloadDto> retrieveTrainersWorkloadForMonth(
      int year, int month, String firstName, String lastName) {

    log.debug("Sending a request to retrieve trainers' workload for certain month: {}, {}",
        year, Month.of(month));

    String correlationID = UUID.randomUUID().toString();
    jmsTemplate.send(retrieveTrainerWorkloadRequestQueue, session -> {
      Message message = session.createMessage();
      message.setIntProperty("year", year);
      message.setIntProperty("month", month);
      message.setStringProperty("firstName", firstName);
      message.setStringProperty("lastName", lastName);
      message.setJMSCorrelationID(correlationID);
      return message;
    });

    TrainerWorkloadDtoList response = (TrainerWorkloadDtoList) jmsTemplate
        .receiveSelectedAndConvert(
            retrieveTrainerWorkloadResponseQueue,
            String.format("JMSCorrelationID = '%s'", correlationID)
        );

    return response != null
        ? response.getItems()
        : TrainerWorkloadDtoList.getFallbackList(year, month, firstName, lastName).getItems();
  }

  public List<TrainerWorkloadDto> retrieveTrainersWorkloadForMonthFailure(
      int year, int month, String firstName, String lastName, Exception ex) {
    return TrainerWorkloadDtoList.getFallbackList(year, month, firstName, lastName).getItems();
  }

  @Retry(name = "reportsMicroserviceRetry")
  @CircuitBreaker(name = "reportsMicroserviceCircuitBreaker",
      fallbackMethod = "updateTrainerWorkloadFailure")
  public void updateTrainerWorkload(Training training, ActionType actionType) {
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto =
        trainerMapper.toTrainerWorkloadUpdateDto(training, actionType);

    updateTrainerWorkload(Collections.singletonList(trainerWorkloadUpdateDto));
  }

  @Retry(name = "reportsMicroserviceRetry")
  @CircuitBreaker(name = "reportsMicroserviceCircuitBreaker",
      fallbackMethod = "updateTrainerWorkloadFailure")
  public void updateTrainerWorkload(List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos) {
    log.debug("Sending a request to update trainers' workload");

    jmsTemplate.convertAndSend(updateTrainerWorkloadQueue,
        new TrainerWorkloadUpdateDtoList(trainerWorkloadUpdateDtos),
        message -> {
          message.setStringProperty(transactionIdHeader, MDC.get(transactionIdKey));
          return message;
        });
  }

  public void updateTrainerWorkloadFailure(Exception ex) {
    throw new TrainerWorkloadUpdateException();
  }
}
