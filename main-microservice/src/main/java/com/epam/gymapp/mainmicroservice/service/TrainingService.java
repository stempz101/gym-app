package com.epam.gymapp.mainmicroservice.service;

import com.epam.gymapp.mainmicroservice.dto.TrainingCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.exception.TraineeNotFoundException;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.mapper.TrainingMapper;
import com.epam.gymapp.mainmicroservice.model.Trainee;
import com.epam.gymapp.mainmicroservice.model.Trainer;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.mainmicroservice.producer.ReportsProducer;
import com.epam.gymapp.mainmicroservice.repository.TraineeRepository;
import com.epam.gymapp.mainmicroservice.repository.TrainerRepository;
import com.epam.gymapp.mainmicroservice.repository.TrainingRepository;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
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

  private final ReportsProducer reportsProducer;

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

    reportsProducer.updateTrainerWorkload(training, ActionType.ADD);

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
