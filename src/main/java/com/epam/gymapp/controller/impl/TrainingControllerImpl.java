package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TrainingController;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.service.TrainingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainingControllerImpl implements TrainingController {

  private static final Logger log = LoggerFactory.getLogger(TrainingControllerImpl.class);

  private final TrainingService trainingService;

  @Override
  public void addTraining(TrainingCreateDto trainingCreateDto) {
    log.info("Starting to add a new Training for Trainee (username={}) and Trainer (username={})",
        trainingCreateDto.getTraineeUsername(), trainingCreateDto.getTrainerUsername());
    trainingService.addTraining(trainingCreateDto);
  }

  @Override
  public List<TrainingInfoDto> selectTrainings() {
    log.info("Starting to select a list of Trainings");
    return trainingService.selectTrainings();
  }
}
