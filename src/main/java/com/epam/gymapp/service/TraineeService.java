package com.epam.gymapp.service;

import com.epam.gymapp.dao.TraineeDAO;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.exception.TraineeExistsWithUsernameException;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.mapper.TraineeMapper;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.utils.UserUtils;
import com.epam.gymapp.validator.TraineeCreateDtoValidator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeService {

  private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

  @Autowired
  private TraineeDAO traineeDAO;

  private TraineeCreateDtoValidator traineeCreateDtoValidator;
  private TraineeMapper traineeMapper;

  public Trainee create(TraineeCreateDto traineeCreateDto) {
    log.info("Creating Trainee: {}", traineeCreateDto);

    traineeCreateDtoValidator.validate(traineeCreateDto);

    Trainee trainee = traineeMapper.toTrainee(traineeCreateDto);

    int numOfAppearances = traineeDAO.getLastCountedAppearances(
        traineeCreateDto.getFirstName(), traineeCreateDto.getLastName());
    trainee.setUsername(UserUtils.buildUsername(trainee, numOfAppearances));
    trainee.setPassword(UserUtils.generatePassword());

    return traineeDAO.save(trainee);
  }

  public List<Trainee> selectTrainees() {
    log.info("Selecting all Trainees");

    return traineeDAO.findAll();
  }

  public Trainee selectTrainee(Long id) {
    log.info("Selecting Trainee by ID: {}", id);

    return traineeDAO.findById(id).orElseThrow(() -> new TraineeNotFoundException(id));
  }

  public Trainee update(Trainee trainee) {
    log.info("Updating Trainee: {}", trainee);

    if (trainee == null) {
      throw new IllegalArgumentException("Trainee must not be null");
    } else if (!traineeDAO.existsById(trainee.getUserId())) {
      throw new TraineeNotFoundException(trainee.getUserId());
    } else if (stringIsNotEmpty(trainee.getUsername())
        && traineeDAO.existsByUsername(trainee.getUsername())) {
      throw new TraineeExistsWithUsernameException(trainee.getUsername());
    }

    return traineeDAO.save(trainee);
  }

  private boolean stringIsNotEmpty(String str) {
    return str != null && !str.isBlank();
  }

  public void delete(Long id) {
    log.info("Deleting Trainee by ID: {}", id);

    traineeDAO.deleteById(id);
  }

  @Autowired
  public void setTraineeCreateDtoValidator(TraineeCreateDtoValidator traineeCreateDtoValidator) {
    this.traineeCreateDtoValidator = traineeCreateDtoValidator;
  }

  @Autowired
  public void setTraineeMapper(TraineeMapper traineeMapper) {
    this.traineeMapper = traineeMapper;
  }
}
