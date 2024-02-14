package com.epam.gymapp.service;

import com.epam.gymapp.dao.TraineeDAO;
import com.epam.gymapp.dao.TrainerDAO;
import com.epam.gymapp.dao.TrainingDAO;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.TrainingNotFoundException;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.validator.TrainingCreateDtoValidator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {

  private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

  @Autowired
  private TrainingDAO trainingDAO;
  @Autowired
  private TraineeDAO traineeDAO;
  @Autowired
  private TrainerDAO trainerDAO;

  private TrainingCreateDtoValidator trainingCreateDtoValidator;
  private TrainingMapper trainingMapper;

  public Training create(TrainingCreateDto trainingCreateDto) {
    log.info("Creating Training: {}", trainingCreateDto);

    trainingCreateDtoValidator.validate(trainingCreateDto);

    if (!traineeDAO.existsById(trainingCreateDto.getTraineeId())) {
      throw new TraineeNotFoundException(trainingCreateDto.getTraineeId());
    } else if (!trainerDAO.existsById(trainingCreateDto.getTrainerId())) {
      throw new TrainerNotFoundException(trainingCreateDto.getTrainerId());
    }

    return trainingDAO.save(trainingMapper.toTraining(trainingCreateDto));
  }

  public List<Training> selectTrainings() {
    log.info("Selecting all Trainings");

    return trainingDAO.findAll();
  }

  public Training selectTraining(Long id) {
    log.info("Selecting Training by ID: {}", id);

    return trainingDAO.findById(id).orElseThrow(() -> new TrainingNotFoundException(id));
  }

  @Autowired
  public void setTrainingCreateDtoValidator(TrainingCreateDtoValidator trainingCreateDtoValidator) {
    this.trainingCreateDtoValidator = trainingCreateDtoValidator;
  }

  @Autowired
  public void setTrainingMapper(TrainingMapper trainingMapper) {
    this.trainingMapper = trainingMapper;
  }
}
