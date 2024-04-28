package com.epam.gymapp.mainmicroservice.controller.impl;

import com.epam.gymapp.mainmicroservice.controller.TraineeController;
import com.epam.gymapp.mainmicroservice.controller.utils.ControllerUtils;
import com.epam.gymapp.mainmicroservice.dto.TraineeInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.service.TraineeService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeController {

  private static final Logger log = LoggerFactory.getLogger(TraineeControllerImpl.class);

  private final TraineeService traineeService;

  @Override
  public List<TraineeInfoDto> selectTrainees() {
    log.info("Starting to select a list of Trainees");
    return traineeService.selectTrainees();
  }

  @Override
  public TraineeInfoDto selectTrainee(String traineeUsername) {
    log.info("Starting to select a specified Trainee (username={})", traineeUsername);
    return traineeService.selectTrainee(traineeUsername);
  }

  @Override
  public TraineeInfoDto updateTrainee(TraineeUpdateDto traineeUpdateDto) {
    log.info("Starting an update of Trainee (username={})", traineeUpdateDto.getUsername());
    return traineeService.updateTrainee(traineeUpdateDto);
  }

  @Override
  public void deleteTrainee(String traineeUsername) {
    log.info("Starting a deletion of Trainee (username={})", traineeUsername);
    traineeService.deleteTrainee(traineeUsername);
  }

  @Override
  public List<TrainerShortInfoDto> updateTrainerListOfTrainee(
      TraineeTrainersUpdateDto traineeTrainersUpdateDto) {
    log.info("Starting an update of Trainee's (username={}) list of Trainers",
        traineeTrainersUpdateDto.getTraineeUsername());
    return traineeService.updateTrainerList(traineeTrainersUpdateDto);
  }

  @Override
  public List<TrainingInfoDto> getTraineeTrainings(String traineeUsername, String fromDate,
      String toDate, String trainerName, String trainingType) {
    log.info("Starting to select Trainee's (username={}) Trainings", traineeUsername);

    LocalDate parsedFromDate = ControllerUtils.parseStringToLocalDate(fromDate);
    LocalDate parsedToDate = ControllerUtils.parseStringToLocalDate(toDate);

    return traineeService.findTraineeTrainings(traineeUsername, parsedFromDate, parsedToDate,
        trainerName, trainingType);
  }
}
