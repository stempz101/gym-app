package com.epam.gymapp.controller;

import com.epam.gymapp.controller.utils.ControllerUtils;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.TraineeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainees")
@Api(value = "Trainee management API")
@RequiredArgsConstructor
public class TraineeController {

  private static final Logger log = LoggerFactory.getLogger(TraineeController.class);

  private final TraineeService traineeService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @PostMapping
  @ApiOperation(
      value = "Creating trainee",
      response = UserCredentialsDto.class
  )
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Trainee successfully created"),
      @ApiResponse(code = 400, message = "Specified wrong fields"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  @ResponseStatus(HttpStatus.CREATED)
  public UserCredentialsDto createTrainee(
      @RequestBody @Valid TraineeCreateDto traineeCreateDto,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), traineeCreateDto);

      UserCredentialsDto userCredentialsDto = traineeService.createTrainee(traineeCreateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.CREATED.value());
      return userCredentialsDto;
    });
  }

  @GetMapping
  @ApiOperation(
      value = "Selecting trainees",
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of trainees successfully returned"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public List<TraineeInfoDto> selectTrainees(HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TraineeInfoDto> traineeInfoDtos = traineeService.selectTrainees();

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return traineeInfoDtos;
    });
  }

  @GetMapping("/{traineeUsername}")
  @ApiOperation(
      value = "Selecting trainee",
      response = TraineeInfoDto.class,
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Trainee information successfully returned"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 404, message = "Trainee not found"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public TraineeInfoDto selectTrainee(
      @PathVariable String traineeUsername,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      TraineeInfoDto traineeInfoDto = traineeService.selectTrainee(traineeUsername);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return traineeInfoDto;
    });
  }

  @PutMapping
  @ApiOperation(
      value = "Updating trainee",
      response = TraineeInfoDto.class,
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Trainee successfully updated"),
      @ApiResponse(code = 400, message = "Specified wrong fields"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 404, message = "Trainee not found"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public TraineeInfoDto updateTrainee(
      @RequestBody @Valid TraineeUpdateDto traineeUpdateDto,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), traineeUpdateDto);

      jwtProcess.processToken(request);
      TraineeInfoDto traineeInfoDto = traineeService.updateTrainee(traineeUpdateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return traineeInfoDto;
    });
  }

  @DeleteMapping("/{traineeUsername}")
  @ApiOperation(
      value = "Deleting trainee",
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Trainee successfully deleted"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 404, message = "Trainee not found"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public void deleteTrainee(@PathVariable String traineeUsername, HttpServletRequest request) {
    loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      traineeService.deleteTrainee(traineeUsername);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return null;
    });
  }

  @PutMapping("/trainers")
  @ApiOperation(
      value = "Updating trainee's trainer list",
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Trainee's trainer list successfully updated"),
      @ApiResponse(code = 400, message = "Specified wrong fields"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 404, message = "Trainee or trainers not found"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public List<TrainerShortInfoDto> updateTrainerListOfTrainee(
      @RequestBody @Valid TraineeTrainersUpdateDto traineeTrainersUpdateDto,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), traineeTrainersUpdateDto);

      jwtProcess.processToken(request);
      List<TrainerShortInfoDto> trainerShortInfoDtos = traineeService
          .updateTrainerList(traineeTrainersUpdateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainerShortInfoDtos;
    });
  }

  @GetMapping("/trainings")
  @ApiOperation(
      value = "Retrieving trainee's trainings",
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of trainee's trainings successfully returned"),
      @ApiResponse(code = 400, message = "Missing required parameters"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public List<TrainingInfoDto> getTraineeTrainings(
      @RequestParam(name = "username") String traineeUsername,
      @RequestParam(name = "fromDate", required = false) String fromDate,
      @RequestParam(name = "toDate", required = false) String toDate,
      @RequestParam(name = "trainerName", required = false) String trainerName,
      @RequestParam(name = "trainingType", required = false) String trainingType,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      Map<String, List<String>> params = ControllerUtils.getRequestParams(request);
      log.info("REST call received - Endpoint: '{}', Method: {}, Parameters: {}",
          request.getRequestURI(), request.getMethod(), params);

      jwtProcess.processToken(request);

      LocalDate parsedFromDate = ControllerUtils.parseStringToLocalDate(fromDate);
      LocalDate parsedToDate = ControllerUtils.parseStringToLocalDate(toDate);

      List<TrainingInfoDto> traineeTrainings = traineeService
          .findTraineeTrainings(traineeUsername, parsedFromDate, parsedToDate, trainerName, trainingType);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Parameters: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), params, HttpStatus.OK.value());
      return traineeTrainings;
    });
  }
}
