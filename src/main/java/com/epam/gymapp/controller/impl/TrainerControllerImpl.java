package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TrainerController;
import com.epam.gymapp.controller.utils.ControllerUtils;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.service.TrainerService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainerControllerImpl implements TrainerController {

  private final TrainerService trainerService;
  private final JwtProcess jwtProcess;

  @Override
  public UserCredentialsDto createTrainer(TrainerCreateDto trainerCreateDto,
      HttpServletRequest request) {
    return trainerService.createTrainer(trainerCreateDto);
  }

  @Override
  public List<TrainerInfoDto> selectTrainers(HttpServletRequest request) {
    jwtProcess.processToken(request);
    return trainerService.selectTrainers();
  }

  @Override
  public TrainerInfoDto selectTrainer(String trainerUsername, HttpServletRequest request) {
    jwtProcess.processToken(request);
    return trainerService.selectTrainer(trainerUsername);
  }

  @Override
  public TrainerInfoDto updateTrainer(TrainerUpdateDto trainerUpdateDto,
      HttpServletRequest request) {
    jwtProcess.processToken(request);
    return trainerService.updateTrainer(trainerUpdateDto);
  }

  @Override
  public List<TrainerShortInfoDto> getUnassignedTrainersOnTrainee(String traineeUsername,
      HttpServletRequest request) {
    jwtProcess.processToken(request);
    return trainerService.findUnassignedTrainers(traineeUsername);
  }

  @Override
  public List<TrainingInfoDto> getTrainerTrainings(String trainerUsername, String fromDate,
      String toDate, String traineeName, HttpServletRequest request) {
    jwtProcess.processToken(request);

    LocalDate parsedFromDate = ControllerUtils.parseStringToLocalDate(fromDate);
    LocalDate parsedToDate = ControllerUtils.parseStringToLocalDate(toDate);

    return trainerService.findTrainerTrainings(trainerUsername, parsedFromDate, parsedToDate, traineeName);
  }
}
