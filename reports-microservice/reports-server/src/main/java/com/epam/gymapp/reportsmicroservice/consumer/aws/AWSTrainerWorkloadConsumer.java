package com.epam.gymapp.reportsmicroservice.consumer.aws;

import com.epam.gymapp.reportsmicroservice.consumer.TrainerWorkloadConsumer;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.service.TrainerWorkloadService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Profile({"stg", "prod"})
@RequiredArgsConstructor
public class AWSTrainerWorkloadConsumer implements TrainerWorkloadConsumer {

  private static final Logger log = LoggerFactory.getLogger(AWSTrainerWorkloadConsumer.class);

  private final TrainerWorkloadService trainerWorkloadService;

  @Override
  @SqsListener("${application.messaging.queue.update-trainer-workload}")
  public void updateTrainersWorkload(@Valid TrainerWorkloadUpdateDtoList updateDtoList) {
    log.info("Starting an update of trainers' workload: {}", updateDtoList);
    trainerWorkloadService.updateTrainersWorkload(updateDtoList);
  }
}
