package com.epam.gymapp.reportsmicroservice.consumer;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import java.time.Month;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@RequiredArgsConstructor
public class TrainerWorkloadConsumer {

  private static final Logger log = LoggerFactory.getLogger(TrainerWorkloadConsumer.class);

  private final TrainerWorkloadService trainerWorkloadService;

  @JmsListener(destination = "${application.messaging.queue.retrieve-trainer-workload.request}")
  @SendTo(value = "${application.messaging.queue.retrieve-trainer-workload.response}")
  public TrainerWorkloadDtoList retrieveTrainersWorkloadForMonth(
      @Header(value = "year") int year,
      @Header(value = "month") int month,
      @Header(value = "firstName", required = false) String firstName,
      @Header(value = "lastName", required = false) String lastName
  ) {
    log.info("Starting to select trainers' workload for certain month: year={}, month={}, firstName={}, lastName={}",
        year, Month.of(month), firstName, lastName);

    return trainerWorkloadService.retrieveTrainersWorkloadForMonth(year, month, firstName, lastName);
  }

  @JmsListener(destination = "${application.messaging.queue.update-trainer-workload}")
  public void updateTrainersWorkload(@Valid TrainerWorkloadUpdateDtoList updateDtoList) {
    log.info("Starting an update of trainers' workload: {}", updateDtoList);
    trainerWorkloadService.updateTrainersWorkload(updateDtoList);
  }
}
