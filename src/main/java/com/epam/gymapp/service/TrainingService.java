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
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainingService {

  private static final Logger log = LoggerFactory.getLogger(TrainingService.class);

  private final TrainingRepository trainingRepository;
  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;

  private final TrainingMapper trainingMapper;

  private final MeterRegistry meterRegistry;

  @PostConstruct
  public void setUp() {
    Gauge.builder("gymapp_upcoming_trainings",
        () -> trainingRepository.countOfUpcomingTrainings(LocalDate.now()))
        .description("Number of upcoming trainings")
        .register(meterRegistry);
  }

  @Transactional
  public void addTraining(TrainingCreateDto trainingCreateDto) {
    log.info("Creating Training: {}", trainingCreateDto);

    Trainee trainee = traineeRepository.findByUsername(trainingCreateDto.getTraineeUsername())
        .orElseThrow(() -> new TraineeNotFoundException(trainingCreateDto.getTraineeUsername()));
    Trainer trainer = trainerRepository.findByUsername(trainingCreateDto.getTrainerUsername())
        .orElseThrow(() -> new TrainerNotFoundException(trainingCreateDto.getTrainerUsername()));

    Training training = trainingMapper.toTraining(trainingCreateDto);
    training.setTrainee(trainee);
    training.setTrainer(trainer);
    training.setType(trainer.getSpecialization());

    training = trainingRepository.save(training);

    log.info("Training added successfully: {}", training);
  }

  @Transactional(readOnly = true)
  public List<TrainingInfoDto> selectTrainings() {
    log.info("Selecting all Trainings");

    return trainingRepository.findAll().stream()
        .map(trainingMapper::toTrainingInfoDto)
        .toList();
  }
}
