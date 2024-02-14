package com.epam.gymapp.validator;

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.exception.TraineeValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TraineeCreateDtoValidator implements GeneralValidator {

  private static final Logger log = LoggerFactory.getLogger(TraineeCreateDtoValidator.class);

  public void validate(TraineeCreateDto traineeCreateDto) {
    log.debug("Validating Trainee: {}", traineeCreateDto);

    validateObjectNotNull(traineeCreateDto,
        () -> new IllegalArgumentException("TraineeCreateDto must not be null"));
    validateStringNotBlank(traineeCreateDto.getFirstName(),
        () -> new TraineeValidationException("Trainee's first name must be specified"));
    validateStringNotBlank(traineeCreateDto.getLastName(),
        () -> new TraineeValidationException("Trainee's last name must be specified"));
    validateObjectNotNull(traineeCreateDto.getDateOfBirth(),
        () -> new TraineeValidationException("Trainee's date of birth must be specified"));
    validateStringNotBlank(traineeCreateDto.getAddress(),
        () -> new TraineeValidationException("Trainee's address must be specified"));
  }
}
