package com.epam.gymapp.reportsmicroservice.consumer.jms;

import com.epam.gymapp.reportsmicroservice.consumer.TrainerWorkloadConsumer;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@Profile({"default", "dev"})
@RequiredArgsConstructor
public class JMSTrainerWorkloadConsumer implements TrainerWorkloadConsumer {

  private static final Logger log = LoggerFactory.getLogger(JMSTrainerWorkloadConsumer.class);

  private final TrainerWorkloadService trainerWorkloadService;

  @Override
  @JmsListener(destination = "${application.messaging.queue.update-trainer-workload}")
  public void updateTrainersWorkload(@Valid TrainerWorkloadUpdateDtoList updateDtoList) {
    log.info("Starting an update of trainers' workload: {}", updateDtoList);
    trainerWorkloadService.updateTrainersWorkload(updateDtoList);
  }
}
