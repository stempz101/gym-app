package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TraineeController;
import com.epam.gymapp.controller.utils.ControllerUtils;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.service.TraineeService;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TraineeControllerImpl implements TraineeController {

  private final TraineeService traineeService;
  private final JwtProcess jwtProcess;

  @Override
  @Timed("gymapp_create_trainee_timer")
  public UserCredentialsDto createTrainee(TraineeCreateDto traineeCreateDto,
      HttpServletRequest request) {
    return traineeService.createTrainee(traineeCreateDto);
  }

  @Override
  public List<TraineeInfoDto> selectTrainees(HttpServletRequest request) {
    jwtProcess.processToken(request);
    return traineeService.selectTrainees();
  }

  @Override
  public TraineeInfoDto selectTrainee(String traineeUsername, HttpServletRequest request) {
    jwtProcess.processToken(request);
    return traineeService.selectTrainee(traineeUsername);
  }

  @Override
  public TraineeInfoDto updateTrainee(TraineeUpdateDto traineeUpdateDto,
      HttpServletRequest request) {
    jwtProcess.processToken(request);
    return traineeService.updateTrainee(traineeUpdateDto);
  }

  @Override
  public void deleteTrainee(String traineeUsername, HttpServletRequest request) {
    jwtProcess.processToken(request);
    traineeService.deleteTrainee(traineeUsername);
  }

  @Override
  public List<TrainerShortInfoDto> updateTrainerListOfTrainee(
      TraineeTrainersUpdateDto traineeTrainersUpdateDto, HttpServletRequest request) {
    jwtProcess.processToken(request);
    return traineeService.updateTrainerList(traineeTrainersUpdateDto);
  }

  @Override
  public List<TrainingInfoDto> getTraineeTrainings(String traineeUsername, String fromDate,
      String toDate, String trainerName, String trainingType, HttpServletRequest request) {
    jwtProcess.processToken(request);

    LocalDate parsedFromDate = ControllerUtils.parseStringToLocalDate(fromDate);
    LocalDate parsedToDate = ControllerUtils.parseStringToLocalDate(toDate);

    return traineeService.findTraineeTrainings(traineeUsername, parsedFromDate, parsedToDate,
        trainerName, trainingType);
  }
}
