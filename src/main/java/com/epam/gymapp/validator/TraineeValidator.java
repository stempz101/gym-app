package com.epam.gymapp.validator;

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.exception.TraineeValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TraineeValidator implements GeneralValidator {

  private static final Logger log = LoggerFactory.getLogger(TraineeValidator.class);

  public void validate(TraineeCreateDto traineeCreateDto) {
    log.debug("Validating Trainee: {}", traineeCreateDto);

    validateObjectNotNull(traineeCreateDto,
        () -> new IllegalArgumentException("TraineeCreateDto must not be null"));
    validateStringNotBlank(traineeCreateDto.getFirstName(),
        () -> new TraineeValidationException("Trainee's first name must be specified"));
    validateStringNotBlank(traineeCreateDto.getLastName(),
        () -> new TraineeValidationException("Trainee's last name must be specified"));
  }

  public void validate(TraineeUpdateDto traineeUpdateDto) {
    log.debug("Validating Trainee: {}", traineeUpdateDto);

    validateObjectNotNull(traineeUpdateDto,
        () -> new IllegalArgumentException("TraineeUpdateDto must not be null"));
    validateStringNotBlank(traineeUpdateDto.getUsername(),
        () -> new TraineeValidationException("Trainee's username must be specified"));
    validateStringNotBlank(traineeUpdateDto.getFirstName(),
        () -> new TraineeValidationException("Trainee's first name must be specified"));
    validateStringNotBlank(traineeUpdateDto.getLastName(),
        () -> new TraineeValidationException("Trainee's last name must be specified"));
  }
}
