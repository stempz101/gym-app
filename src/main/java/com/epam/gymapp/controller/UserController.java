package com.epam.gymapp.controller;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.JwtDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Api(value = "User management API")
@RequiredArgsConstructor
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;
  private final JwtProcess jwtProcess;

  private final LoggerHelper loggerHelper;

  @PostMapping("/authenticate")
  @ApiOperation(
      value = "Authenticate user",
      notes = "You can specify a regular String instead of array of chars as password"
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully authenticated"),
      @ApiResponse(code = 400, message = "Specified wrong username or password"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public JwtDto authenticate(
      @RequestBody @Valid UserCredentialsDto userCredentialsDto,
      HttpServletRequest request
  ) {
    return loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), userCredentialsDto);

      JwtDto jwtDto = userService.authenticate(userCredentialsDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return jwtDto;
    });
  }

  @PutMapping("/change-password")
  @ApiOperation(
      value = "Change user password",
      notes = "You can specify a regular String instead of array of chars as password"
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Password changed successfully"),
      @ApiResponse(code = 400, message = "Specified wrong username or passwords"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public void changePassword(
      @RequestBody @Valid ChangePasswordDto changePasswordDto,
      HttpServletRequest request
  ) {
    loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), changePasswordDto);

      userService.changePassword(changePasswordDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return null;
    });
  }

  @PatchMapping("/change-activation-status")
  @ApiOperation(
      value = "Change user activation status",
      authorizations = @Authorization(value = "bearer")
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Activation status changed successfully"),
      @ApiResponse(code = 400, message = "Specified wrong fields"),
      @ApiResponse(code = 401, message = "Attempted unauthorized access"),
      @ApiResponse(code = 404, message = "User not found"),
      @ApiResponse(code = 500, message = "Application failed to process the request")
  })
  public void changeActivationStatus(
      @RequestBody @Valid UserActivateDto userActivateDto,
      HttpServletRequest request
  ) {
    loggerHelper.transactionalLogging(() -> {
      log.info("REST call received - Endpoint: '{}', Method: {}, Body: {}",
          request.getRequestURI(), request.getMethod(), userActivateDto);

      jwtProcess.processToken(request);
      userService.changeActivationStatus(userActivateDto);

      log.info("REST call completed - Endpoint: '{}', Method: {}, Status: {}",
          request.getRequestURI(), request.getMethod(), HttpStatus.OK.value());
      return null;
    });
  }
}
