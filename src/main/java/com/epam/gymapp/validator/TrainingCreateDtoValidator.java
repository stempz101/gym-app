package com.epam.gymapp.validator;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.exception.TrainingValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrainingCreateDtoValidator implements GeneralValidator {

  private static final Logger log = LoggerFactory.getLogger(TrainingCreateDtoValidator.class);

  public void validate(TrainingCreateDto trainingCreateDto) {
    log.debug("Validating Training: {}", trainingCreateDto);

    validateObjectNotNull(trainingCreateDto,
        () -> new IllegalArgumentException("TrainingCreateDto must not be null"));
    validateObjectNotNull(trainingCreateDto.getTraineeId(),
        () -> new TrainingValidationException("Trainee ID must be specified"));
    validateObjectNotNull(trainingCreateDto.getTrainerId(),
        () -> new TrainingValidationException("Trainer ID must be specified"));
    validateStringNotBlank(trainingCreateDto.getName(),
        () -> new TrainingValidationException("Training name must be specified"));
    validateTrainingType(trainingCreateDto.getType(),
        () -> new TrainingValidationException("Training type must be specified"));
    validateObjectNotNull(trainingCreateDto.getDate(),
        () -> new TrainingValidationException("Training date must be specified"));
    validateObjectNotNull(trainingCreateDto.getDuration(),
        () -> new TrainingValidationException("Training duration must be specified"));
  }
}
