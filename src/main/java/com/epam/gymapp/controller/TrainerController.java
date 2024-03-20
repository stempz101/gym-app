package com.epam.gymapp.controller;

import com.epam.gymapp.controller.utils.ControllerUtils;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.TrainerService;
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
@RequestMapping("/trainers")
@Api(value = "Trainer management API")
@RequiredArgsConstructor
public class TrainerController {

  private static final Logger log = LoggerFactory.getLogger(TrainerController.class);

  private final TrainerService trainerService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @PostMapping
  @ApiOperation(
      value = "Creating trainer",
      response = UserCredentialsDto.class
  )
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Trainer successfully created"),
      @ApiResponse(code = 400, message = "Specified wrong fields"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  @ResponseStatus(HttpStatus.CREATED)
  public UserCredentialsDto createTrainer(
      @RequestBody @Valid TrainerCreateDto trainerCreateDto,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), trainerCreateDto);

      UserCredentialsDto userCredentialsDto = trainerService.createTrainer(trainerCreateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.CREATED.value());
      return userCredentialsDto;
    });
  }

  @GetMapping
  @ApiOperation(
      value = "Selecting trainers",
      response = TraineeInfoDto.class,
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of trainers successfully returned"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public List<TrainerInfoDto> selectTrainers(HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TrainerInfoDto> trainerInfoDtos = trainerService.selectTrainers();

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainerInfoDtos;
    });
  }

  @GetMapping("/{trainerUsername}")
  @ApiOperation(
      value = "Selecting trainer",
      response = TrainerInfoDto.class,
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Trainer information successfully returned"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 404, message = "Trainer not found"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public TrainerInfoDto selectTrainer(
      @PathVariable String trainerUsername,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      TrainerInfoDto trainerInfoDto = trainerService.selectTrainer(trainerUsername);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainerInfoDto;
    });
  }

  @PutMapping
  @ApiOperation(
      value = "Updating trainer",
      response = TraineeInfoDto.class,
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Trainer successfully updated"),
      @ApiResponse(code = 400, message = "Specified wrong fields"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 404, message = "Trainer not found"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public TrainerInfoDto updateTrainer(
      @RequestBody @Valid TrainerUpdateDto trainerUpdateDto,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), trainerUpdateDto);

      jwtProcess.processToken(request);
      TrainerInfoDto trainerInfoDto = trainerService.updateTrainer(trainerUpdateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainerInfoDto;
    });
  }

  @GetMapping("/unassigned/{traineeUsername}")
  @ApiOperation(
      value = "Retrieving unassigned trainers on specified trainee",
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of unassigned trainers successfully returned"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public List<TrainerShortInfoDto> getUnassignedTrainersOnTrainee(
      @PathVariable String traineeUsername,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TrainerShortInfoDto> unassignedTrainers = trainerService
          .findUnassignedTrainers(traineeUsername);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return unassignedTrainers;
    });
  }

  @GetMapping("/trainings")
  @ApiOperation(
      value = "Retrieving trainer's trainings",
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of trainer's trainings successfully returned"),
      @ApiResponse(code = 400, message = "Missing required parameters"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public List<TrainingInfoDto> getTrainerTrainings(
      @RequestParam(name = "username") String trainerUsername,
      @RequestParam(name = "fromDate", required = false) String fromDate,
      @RequestParam(name = "toDate", required = false) String toDate,
      @RequestParam(name = "traineeName", required = false) String traineeName,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      Map<String, List<String>> params = ControllerUtils.getRequestParams(request);
      log.info("REST call received - Endpoint: '{}', Method: {}, Parameters: {}",
          request.getRequestURI(), request.getMethod(), params);

      jwtProcess.processToken(request);

      LocalDate parsedFromDate = ControllerUtils.parseStringToLocalDate(fromDate);
      LocalDate parsedToDate = ControllerUtils.parseStringToLocalDate(toDate);

      List<TrainingInfoDto> trainerTrainings = trainerService
          .findTrainerTrainings(trainerUsername, parsedFromDate, parsedToDate, traineeName);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Parameters: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), params, HttpStatus.OK.value());
      return trainerTrainings;
    });
  }
}
