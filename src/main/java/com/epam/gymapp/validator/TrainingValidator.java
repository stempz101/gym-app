package com.epam.gymapp.validator;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.exception.TrainingValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrainingValidator implements GeneralValidator {

  private static final Logger log = LoggerFactory.getLogger(TrainingValidator.class);

  public void validate(TrainingCreateDto trainingCreateDto) {
    log.debug("Validating Training: {}", trainingCreateDto);

    validateObjectNotNull(trainingCreateDto,
        () -> new IllegalArgumentException("TrainingCreateDto must not be null"));
    validateStringNotBlank(trainingCreateDto.getTraineeUsername(),
        () -> new TrainingValidationException("Trainee username must be specified"));
    validateStringNotBlank(trainingCreateDto.getTrainerUsername(),
        () -> new TrainingValidationException("Trainer username must be specified"));
    validateStringNotBlank(trainingCreateDto.getName(),
        () -> new TrainingValidationException("Training name must be specified"));
    validateObjectNotNull(trainingCreateDto.getDate(),
        () -> new TrainingValidationException("Training date must be specified"));
    validateObjectNotNull(trainingCreateDto.getDuration(),
        () -> new TrainingValidationException("Training duration must be specified"));
  }
}
