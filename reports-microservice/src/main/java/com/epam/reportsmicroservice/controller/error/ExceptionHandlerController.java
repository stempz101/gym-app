package com.epam.reportsmicroservice.controller.error;

import com.epam.reportsmicroservice.dto.ErrorMessageDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

  private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

  @Hidden
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Set<ErrorMessageDto> handleConstraintViolationException(ConstraintViolationException ex) {
    log.error("handleMethodArgumentNotValidException: {}", ex.getMessage(), ex);
    return ex.getConstraintViolations().stream()
        .map(violation -> new ErrorMessageDto(violation.getMessage()))
        .collect(Collectors.toSet());
  }

  @Hidden
  @ExceptionHandler(MissingServletRequestParameterException.class)
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

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public void handleException(Exception ex) {
    log.error("handleException: {}", ex.getMessage(), ex);
  }
}
