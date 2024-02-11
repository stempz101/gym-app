package com.epam.gymapp.facade;

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.service.TraineeService;
import com.epam.gymapp.service.TrainerService;
import com.epam.gymapp.service.TrainingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GymFacade {

  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingService trainingService;

  public Trainee createTraineeProfile(TraineeCreateDto traineeCreateDto) {
    return traineeService.create(traineeCreateDto);
  }

  public Trainee updateTraineeProfile(Trainee trainee) {
    return traineeService.update(trainee);
  }

  public List<Trainee> selectAllTrainees() {
    return traineeService.selectTrainees();
  }

  public Trainee selectTraineeProfile(Long id) {
    return traineeService.selectTrainee(id);
  }

  public void deleteTraineeProfile(Long id) {
    traineeService.delete(id);
  }

  public Trainer createTrainerProfile(TrainerCreateDto trainerCreateDto) {
    return trainerService.create(trainerCreateDto);
  }

  public Trainer updateTrainerProfile(Trainer trainer) {
    return trainerService.update(trainer);
  }

  public List<Trainer> selectAllTrainers() {
    return trainerService.selectTrainers();
  }

  public Trainer selectTrainerProfile(Long id) {
    return trainerService.selectTrainer(id);
  }

  public Training createTraining(TrainingCreateDto trainingCreateDto) {
    return trainingService.create(trainingCreateDto);
  }

  public List<Training> selectAllTrainings() {
    return trainingService.selectTrainings();
  }

  public Training selectTraining(Long id) {
    return trainingService.selectTraining(id);
  }
}
