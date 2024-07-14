package com.epam.gymapp.mainmicroservice.producer.aws;

import com.epam.gymapp.mainmicroservice.mapper.TrainerMapper;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.mainmicroservice.producer.ReportsProducer;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"stg", "prod"})
@RequiredArgsConstructor
public class AWSReportsProducer implements ReportsProducer {

  private static final Logger log = LoggerFactory.getLogger(AWSReportsProducer.class);

  private final TrainerMapper trainerMapper;
  private final SqsTemplate sqsTemplate;

  @Value("${application.logging.transaction-id.key}")
  private String transactionIdKey;

  @Value("${application.logging.transaction-id.header}")
  private String transactionIdHeader;

  @Value("${application.messaging.queue.update-trainer-workload}")
  private String updateTrainerWorkloadQueue;

  @Override
  @Retry(name = "reportsMicroserviceRetry")
  @CircuitBreaker(name = "reportsMicroserviceCircuitBreaker",
      fallbackMethod = "updateTrainerWorkloadFailure")
  public void updateTrainerWorkload(Training training, ActionType actionType) {
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto =
        trainerMapper.toTrainerWorkloadUpdateDto(training, actionType);

    updateTrainerWorkload(Collections.singletonList(trainerWorkloadUpdateDto));
  }

  @Override
  @Retry(name = "reportsMicroserviceRetry")
  @CircuitBreaker(name = "reportsMicroserviceCircuitBreaker",
      fallbackMethod = "updateTrainerWorkloadFailure")
  public void updateTrainerWorkload(List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos) {
    log.debug("Sending a request to update trainers' workload");

    Map<String, Object> headers = new HashMap<>();
    headers.put(transactionIdHeader, MDC.get(transactionIdKey));

    sqsTemplate.send(to -> to.queue(updateTrainerWorkloadQueue)
        .payload(new TrainerWorkloadUpdateDtoList(trainerWorkloadUpdateDtos))
        .headers(headers));
  }
}
