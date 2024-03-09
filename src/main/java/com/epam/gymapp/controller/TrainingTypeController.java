package com.epam.gymapp.controller;

import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.service.TrainingTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training-types")
@Api(value = "Training type management API")
@RequiredArgsConstructor
public class TrainingTypeController {

  private static final Logger log = LoggerFactory.getLogger(TrainingTypeController.class);

  private final TrainingTypeService trainingTypeService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @ApiOperation(value = "Selecting training types", authorizations = @Authorization(value = "bearer"))
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "List of training types successfully returned"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  @GetMapping
  public List<TrainingType> selectTrainingTypes(HttpServletRequest request) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}",
          request.getRequestURI(), request.getMethod());

      jwtProcess.processToken(request);
      List<TrainingType> trainingTypes = trainingTypeService.selectTrainingTypes();

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return trainingTypes;
    });
  }
}
