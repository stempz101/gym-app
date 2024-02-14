package com.epam.gymapp.service;

import com.epam.gymapp.dao.TrainerDAO;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.exception.TrainerExistsWithUsernameException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.mapper.TrainerMapper;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.utils.UserUtils;
import com.epam.gymapp.validator.TrainerCreateDtoValidator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {

  private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

  @Autowired
  private TrainerDAO trainerDAO;

  private TrainerCreateDtoValidator trainerCreateDtoValidator;
  private TrainerMapper trainerMapper;

  public Trainer create(TrainerCreateDto trainerCreateDto) {
    log.info("Creating Trainer: {}", trainerCreateDto);

    trainerCreateDtoValidator.validate(trainerCreateDto);

    Trainer trainer = trainerMapper.toTrainer(trainerCreateDto);

    int numOfAppearances = trainerDAO.getLastCountedAppearances(
        trainerCreateDto.getFirstName(), trainerCreateDto.getLastName());
    trainer.setUsername(UserUtils.buildUsername(trainer, numOfAppearances));
    trainer.setPassword(UserUtils.generatePassword());

    return trainerDAO.save(trainer);
  }

  public List<Trainer> selectTrainers() {
    log.info("Selecting all Trainers");

    return trainerDAO.findAll();
  }

  public Trainer selectTrainer(Long id) {
    log.info("Selecting Trainer by ID: {}", id);

    return trainerDAO.findById(id).orElseThrow(() -> new TrainerNotFoundException(id));
  }

  public Trainer update(Trainer trainer) {
    log.info("Updating Trainer: {}", trainer);

    if (trainer == null) {
      throw new IllegalArgumentException("Trainer must not be null");
    } else if (!trainerDAO.existsById(trainer.getUserId())) {
      throw new TrainerNotFoundException(trainer.getUserId());
    } else if (stringIsNotEmpty(trainer.getUsername())
        && trainerDAO.existsByUsername(trainer.getUsername())) {
      throw new TrainerExistsWithUsernameException(trainer.getUsername());
    }

    return trainerDAO.save(trainer);
  }

  private boolean stringIsNotEmpty(String str) {
    return str != null && !str.isBlank();
  }

  @Autowired
  public void setTrainerCreateDtoValidator(TrainerCreateDtoValidator trainerCreateDtoValidator) {
    this.trainerCreateDtoValidator = trainerCreateDtoValidator;
  }

  @Autowired
  public void setTrainerMapper(TrainerMapper trainerMapper) {
    this.trainerMapper = trainerMapper;
  }
}
