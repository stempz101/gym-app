package com.epam.gymapp.controller;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainings")
@Api(value = "Training management API")
@RequiredArgsConstructor
public class TrainingController {

  private static final Logger log = LoggerFactory.getLogger(TrainingController.class);

  private final TrainingService trainingService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @PostMapping
  @ApiOperation(
      value = "Adding training",
      notes = "Specify training duration in minutes",
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Training successfully added"),
      @ApiResponse(code = 400, message = "Specified wrong fields"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 404, message = "Trainee or trainer not found"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public void addTraining(
      @RequestBody @Valid TrainingCreateDto trainingCreateDto,
      HttpServletRequest request
  ) {
    loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), trainingCreateDto);

      jwtProcess.processToken(request);
      trainingService.addTraining(trainingCreateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return null;
    });
  }

  @GetMapping
  @ApiOperation(value = "Selecting trainings", authorizations = @Authorization(value = "bearer"))
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of trainings successfully returned"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public List<TrainingInfoDto> selectTrainings(HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TrainingInfoDto> trainings = trainingService.selectTrainings();

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainings;
    });
  }
}
