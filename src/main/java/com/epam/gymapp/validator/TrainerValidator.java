package com.epam.gymapp.validator;

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.exception.TrainerValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrainerValidator implements GeneralValidator {

  private static final Logger log = LoggerFactory.getLogger(TrainerValidator.class);

  public void validate(TrainerCreateDto trainerCreateDto) {
    log.debug("Validating Trainer: {}", trainerCreateDto);

    validateObjectNotNull(trainerCreateDto,
        () -> new IllegalArgumentException("TrainerCreateDto must not be null"));
    validateStringNotBlank(trainerCreateDto.getFirstName(),
        () -> new TrainerValidationException("Trainer's first name must be specified"));
    validateStringNotBlank(trainerCreateDto.getLastName(),
        () -> new TrainerValidationException("Trainer's last name must be specified"));
    validateStringNotBlank(trainerCreateDto.getSpecialization(),
        () -> new TrainerValidationException("Trainer's specialization must be specified"));
  }

  public void validate(TrainerUpdateDto trainerUpdateDto) {
    log.debug("Validating Trainer: {}", trainerUpdateDto);

    validateObjectNotNull(trainerUpdateDto,
        () -> new IllegalArgumentException("TrainerUpdateDto must not be null"));
    validateStringNotBlank(trainerUpdateDto.getUsername(),
        () -> new TrainerValidationException("Trainer's username must be specified"));
    validateStringNotBlank(trainerUpdateDto.getFirstName(),
        () -> new TrainerValidationException("Trainer's first name must be specified"));
    validateStringNotBlank(trainerUpdateDto.getLastName(),
        () -> new TrainerValidationException("Trainer's last name must be specified"));
    validateStringNotBlank(trainerUpdateDto.getSpecialization(),
        () -> new TrainerValidationException("Trainer's specialization must be specified"));
  }
}
