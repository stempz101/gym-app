package com.epam.gymapp.service;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.repository.TraineeRepository;
import com.epam.gymapp.repository.TrainerRepository;
import com.epam.gymapp.repository.TrainingRepository;
import com.epam.gymapp.validator.TrainingValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingService {

  private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

  private final TrainingRepository trainingRepository;
  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;

  private final TrainingValidator trainingValidator;

  private final TrainingMapper trainingMapper;

  public TrainingInfoDto createTraining(TrainingCreateDto trainingCreateDto) {
    log.info("Creating Training: {}", trainingCreateDto);

    trainingValidator.validate(trainingCreateDto);

    Trainee trainee = traineeRepository.findByUsername(trainingCreateDto.getTraineeUsername())
        .orElseThrow(() -> new TraineeNotFoundException(trainingCreateDto.getTraineeUsername()));
    Trainer trainer = trainerRepository.findByUsername(trainingCreateDto.getTrainerUsername())
        .orElseThrow(() -> new TrainerNotFoundException(trainingCreateDto.getTrainerUsername()));

    Training training = trainingMapper.toTraining(trainingCreateDto);
    training.setTrainee(trainee);
    training.setTrainer(trainer);
    training.setType(trainer.getSpecialization());

    training = trainingRepository.save(training);
    return trainingMapper.toTrainingInfoDto(training);
  }

  public List<TrainingInfoDto> selectTrainings() {
    log.info("Selecting all Trainings");

    return trainingRepository.findAll().stream()
        .map(trainingMapper::toTrainingInfoDto)
        .toList();
  }
}
