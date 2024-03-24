package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TrainerController;
import com.epam.gymapp.controller.utils.ControllerUtils;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.service.TrainerService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainerControllerImpl implements TrainerController {

  private static final Logger log = LoggerFactory.getLogger(TrainerControllerImpl.class);

  private final TrainerService trainerService;

  @Override
  public List<TrainerInfoDto> selectTrainers() {
    log.info("Starting to select a list of Trainers");
    return trainerService.selectTrainers();
  }

  @Override
  public TrainerInfoDto selectTrainer(String trainerUsername) {
    log.info("Starting to select a specified Trainer (username={})", trainerUsername);
    return trainerService.selectTrainer(trainerUsername);
  }

  @Override
  public TrainerInfoDto updateTrainer(TrainerUpdateDto trainerUpdateDto) {
    log.info("Starting an update of Trainer (username={})", trainerUpdateDto.getUsername());
    return trainerService.updateTrainer(trainerUpdateDto);
  }

  @Override
  public List<TrainerShortInfoDto> getUnassignedTrainersOnTrainee(String traineeUsername) {
    log.info("Starting to get unassigned Trainers on Trainee (username={})", traineeUsername);
    return trainerService.findUnassignedTrainers(traineeUsername);
  }

  @Override
  public List<TrainingInfoDto> getTrainerTrainings(String trainerUsername, String fromDate,
      String toDate, String traineeName) {
    log.info("Starting to select Trainer's (username={}) Trainings", trainerUsername);

    LocalDate parsedFromDate = ControllerUtils.parseStringToLocalDate(fromDate);
    LocalDate parsedToDate = ControllerUtils.parseStringToLocalDate(toDate);

    return trainerService.findTrainerTrainings(trainerUsername, parsedFromDate, parsedToDate, traineeName);
  }
}
