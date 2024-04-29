package com.epam.gymapp.mainmicroservice.controller.impl;

import com.epam.gymapp.mainmicroservice.controller.AuthenticationController;
import com.epam.gymapp.mainmicroservice.dto.ChangePasswordDto;
import com.epam.gymapp.mainmicroservice.dto.JwtDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerCreateDto;
import com.epam.gymapp.mainmicroservice.dto.UserCredentialsDto;
import com.epam.gymapp.mainmicroservice.service.TraineeService;
import com.epam.gymapp.mainmicroservice.service.TrainerService;
import com.epam.gymapp.mainmicroservice.service.UserService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationControllerImpl.class);

  private final UserService userService;
  private final TraineeService traineeService;
  private final TrainerService trainerService;

  @Override
  public JwtDto authenticate(UserCredentialsDto userCredentialsDto) {
    log.info("Starting User authentication");
    return userService.authenticate(userCredentialsDto);
  }

  @Override
  @Timed("gymapp_create_trainee_timer")
  public UserCredentialsDto registerTrainee(TraineeCreateDto traineeCreateDto) {
    log.info("Starting new Trainee registration");
    return traineeService.createTrainee(traineeCreateDto);
  }

  @Override
  public UserCredentialsDto registerTrainer(TrainerCreateDto trainerCreateDto) {
    log.info("Starting new Trainer registration");
    return trainerService.createTrainer(trainerCreateDto);
  }

  @Override
  public void changePassword(ChangePasswordDto changePasswordDto) {
    log.info("Starting the change password process");
    userService.changePassword(changePasswordDto);
  }
}
