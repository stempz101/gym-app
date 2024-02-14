package com.epam.gymapp.validator;

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.exception.TrainerValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrainerCreateDtoValidator implements GeneralValidator {

  private static final Logger log = LoggerFactory.getLogger(TrainerCreateDtoValidator.class);

  public void validate(TrainerCreateDto trainerCreateDto) {
    log.debug("Validating Trainer: {}", trainerCreateDto);

    validateObjectNotNull(trainerCreateDto,
        () -> new IllegalArgumentException("TrainerCreateDto must not be null"));
    validateStringNotBlank(trainerCreateDto.getFirstName(),
        () -> new TrainerValidationException("Trainer's first name must be specified"));
    validateStringNotBlank(trainerCreateDto.getLastName(),
        () -> new TrainerValidationException("Trainer's last name must be specified"));
    validateTrainingType(trainerCreateDto.getSpecialization(),
        () -> new TrainerValidationException("Trainer's specialization must be specified"));
  }
}
