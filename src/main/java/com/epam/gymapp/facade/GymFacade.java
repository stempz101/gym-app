package com.epam.gymapp.facade;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.service.TraineeService;
import com.epam.gymapp.service.TrainerService;
import com.epam.gymapp.service.TrainingService;
import com.epam.gymapp.service.UserService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GymFacade {

  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingService trainingService;
  private final UserService userService;

  public UserCredentialsDto createTraineeProfile(TraineeCreateDto traineeCreateDto) {
    return traineeService.createTrainee(traineeCreateDto);
  }

  public UserCredentialsDto createTrainerProfile(TrainerCreateDto trainerCreateDto) {
    return trainerService.createTrainer(trainerCreateDto);
  }

  public List<TraineeInfoDto> selectAllTrainees(UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return traineeService.selectTrainees();
  }

  public List<TrainerInfoDto> selectAllTrainers(UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return trainerService.selectTrainers();
  }

  public TraineeInfoDto selectTraineeProfile(String username, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return traineeService.selectTrainee(username);
  }

  public TrainerInfoDto selectTrainerProfile(String username, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return trainerService.selectTrainer(username);
  }

  public void changePassword(ChangePasswordDto changePasswordDto) {
    userService.changePassword(changePasswordDto);
  }

  public TraineeInfoDto updateTraineeProfile(
      TraineeUpdateDto traineeUpdateDto, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return traineeService.updateTrainee(traineeUpdateDto);
  }

  public TrainerInfoDto updateTrainerProfile(
      TrainerUpdateDto trainerUpdateDto, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return trainerService.updateTrainer(trainerUpdateDto);
  }

  public void changeActivationStatus(
      UserActivateDto userActivateDto, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    userService.changeActivationStatus(userActivateDto);
  }

  public void deleteTraineeProfile(String username, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    traineeService.deleteTrainee(username);
  }

  public List<TrainingInfoDto> getTraineeTrainings(String username, LocalDate fromDate,
      LocalDate toDate, String trainerName, String trainingType, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return traineeService.findTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
  }

  public List<TrainingInfoDto> getTrainerTrainings(String username, LocalDate fromDate,
      LocalDate toDate, String traineeName, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return trainerService.findTrainerTrainings(username, fromDate, toDate, traineeName);
  }

  public TrainingInfoDto addTraining(
      TrainingCreateDto trainingCreateDto, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return trainingService.createTraining(trainingCreateDto);
  }

  public List<TrainingInfoDto> selectAllTrainings(UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return trainingService.selectTrainings();
  }

  public List<TrainerShortInfoDto> getUnassignedTrainersOnTrainee(
      String username, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return trainerService.findUnassignedTrainers(username);
  }

  public List<TrainerShortInfoDto> updateTrainerListOfTrainee(
      TraineeTrainersUpdateDto traineeTrainersUpdateDto, UserCredentialsDto userCredentialsDto) {
    userService.authenticate(userCredentialsDto);
    return traineeService.updateTrainerList(traineeTrainersUpdateDto);
  }
}
