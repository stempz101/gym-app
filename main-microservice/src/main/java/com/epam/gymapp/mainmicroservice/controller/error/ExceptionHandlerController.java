package com.epam.gymapp.mainmicroservice.controller.error;

import com.epam.gymapp.mainmicroservice.dto.ErrorMessageDto;
import com.epam.gymapp.mainmicroservice.exception.AuthenticationBlockedException;
import com.epam.gymapp.mainmicroservice.exception.ParsingException;
import com.epam.gymapp.mainmicroservice.exception.TraineeNotFoundException;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.exception.TrainerWorkloadUpdateException;
import com.epam.gymapp.mainmicroservice.exception.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

  private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

  @Hidden
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorMessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    log.error("handleMethodArgumentNotValidException: {}", ex.getMessage(), ex);
    return ex.getBindingResult().getAllErrors().stream()
        .map(err -> new ErrorMessageDto(err.getDefaultMessage()))
        .toList();
  }

  @Hidden
  @ExceptionHandler({
      BadCredentialsException.class,
      MissingServletRequestParameterException.class,
      ParsingException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorMessageDto> handleBadRequestException(Exception ex) {
    log.error("handleBadRequestException: {}", ex.getMessage(), ex);
    return Collections.singletonList(new ErrorMessageDto(ex.getMessage()));
  }

  @Hidden
  @ExceptionHandler({
      AuthenticationException.class,
      ExpiredJwtException.class
  })
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public List<ErrorMessageDto> handleUnauthorizedException(Exception ex) {
    log.error("handleUnauthorizedException: {}", ex.getMessage(), ex);
    return Collections.singletonList(new ErrorMessageDto(ex.getMessage()));
  }

  @Hidden
  @ExceptionHandler({
      TraineeNotFoundException.class,
      TrainerNotFoundException.class,
      UserNotFoundException.class
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public List<ErrorMessageDto> handleNotFoundException(Exception ex) {
    log.error("handleNotFoundException: {}", ex.getMessage(), ex);
    return Collections.singletonList(new ErrorMessageDto(ex.getMessage()));
  }

  @Hidden
  @ExceptionHandler(TrainerWorkloadUpdateException.class)
  @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
  public List<ErrorMessageDto> handleTrainerWorkingHoursUpdateException(Exception ex) {
    log.error("handleTrainerWorkingHoursUpdateException: {}", ex.getMessage(), ex);
    return Collections.singletonList(new ErrorMessageDto(ex.getMessage()));
  }

  @Hidden
  @ExceptionHandler(InternalAuthenticationServiceException.class)
  public ResponseEntity<List<ErrorMessageDto>> handleInternalAuthenticationServiceException(Exception ex) {
    log.error("handleInternalAuthenticationServiceException: {}", ex.getMessage(), ex);

    if (ex.getCause() instanceof AuthenticationBlockedException) {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .body(Collections.singletonList(new ErrorMessageDto(ex.getMessage())));
    }

    return ResponseEntity.internalServerError().build();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public void handleException(Exception ex) {
    log.error("handleException: {}", ex.getMessage(), ex);
  }
}
