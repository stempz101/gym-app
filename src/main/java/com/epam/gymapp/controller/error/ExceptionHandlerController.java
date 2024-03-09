package com.epam.gymapp.controller.error;

import com.epam.gymapp.dto.ErrorMessageDto;
import com.epam.gymapp.exception.BadCredentialsException;
import com.epam.gymapp.exception.ParsingException;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.UnauthorizedException;
import com.epam.gymapp.exception.UserNotFoundException;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

  private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorMessageDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    List<ErrorMessageDto> errorResponse = ex.getBindingResult().getAllErrors().stream()
        .map(err -> new ErrorMessageDto(err.getDefaultMessage()))
        .toList();

    log.error("REST call failed - Endpoint: '{}', Method: {}, Status: {}, Response: {}",
        request.getRequestURI(), request.getMethod(), HttpStatus.BAD_REQUEST.value(),  errorResponse, ex);

    MDC.clear();

    return errorResponse;
  }

  @ExceptionHandler({
      BadCredentialsException.class,
      MissingServletRequestParameterException.class,
      ParsingException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorMessageDto> handleBadRequestException(Exception ex, HttpServletRequest request) {
    List<ErrorMessageDto> errorResponse = Collections.singletonList(
        new ErrorMessageDto(ex.getMessage()));

    log.error("REST call failed - Endpoint: '{}', Method: {}, Status: {}, Response: {}",
        request.getRequestURI(), request.getMethod(), HttpStatus.BAD_REQUEST.value(),  errorResponse, ex);

    MDC.clear();

    return errorResponse;
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public List<ErrorMessageDto> handleUnauthorizedException(Exception ex, HttpServletRequest request) {
    List<ErrorMessageDto> errorResponse = Collections.singletonList(
        new ErrorMessageDto(ex.getMessage()));

    log.error("REST call failed - Endpoint: '{}', Method: {}, Status: {}, Response: {}",
        request.getRequestURI(), request.getMethod(), HttpStatus.UNAUTHORIZED.value(),  errorResponse, ex);

    MDC.clear();

    return errorResponse;
  }

  @ExceptionHandler({
      TraineeNotFoundException.class,
      TrainerNotFoundException.class,
      UserNotFoundException.class
  })
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public List<ErrorMessageDto> handleNotFoundException(Exception ex, HttpServletRequest request) {
    List<ErrorMessageDto> errorResponse = Collections.singletonList(
        new ErrorMessageDto(ex.getMessage()));

    log.error("REST call failed - Endpoint: '{}', Method: {}, Status: {}, Response: {}",
        request.getRequestURI(), request.getMethod(), HttpStatus.NOT_FOUND.value(),  errorResponse, ex);

    MDC.clear();

    return errorResponse;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public void handleException(Exception ex, HttpServletRequest request) {
    log.error("REST call failed - Endpoint: '{}', Method: {}, Status: {}",
        request.getRequestURI(), request.getMethod(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex);

    MDC.clear();
  }
}
