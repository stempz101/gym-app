package com.epam.reportsmicroservice.controller.impl;

import com.epam.reportsmicroservice.controller.TrainerWorkloadController;
import com.epam.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.reportsmicroservice.service.TrainerWorkloadService;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainerWorkloadControllerImpl implements TrainerWorkloadController {

  private static final Logger log = LoggerFactory.getLogger(TrainerWorkloadControllerImpl.class);

  private final TrainerWorkloadService trainerWorkloadService;

  @Override
  public List<TrainerWorkloadDto> retrieveTrainersWorkloadForMonth(int year, int month, String username) {
    if (username != null && !username.isBlank()) {
      log.info("Starting to select trainer's workload for certain month (username={}): {}, {}",
          username, year, Month.of(month));
    } else {
      log.info("Starting to select trainers' workload for certain month: {}, {}", year, Month.of(month));
    }

    return trainerWorkloadService.retrieveTrainersWorkloadForMonth(year, month, username);
  }

  @Override
  public void updateTrainersWorkload(List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos) {
    log.info("Starting an update of trainers' workload: {}", trainerWorkloadUpdateDtos);
    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtos);
  }
}
