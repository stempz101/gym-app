package com.epam.gymapp.service;

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.mapper.TrainerMapper;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.User;
import com.epam.gymapp.repository.TrainerRepository;
import com.epam.gymapp.repository.TrainingRepository;
import com.epam.gymapp.repository.UserRepository;
import com.epam.gymapp.utils.UserUtils;
import com.epam.gymapp.validator.TrainerValidator;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerService {

  private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;
  private final TrainingRepository trainingRepository;

  private final TrainerValidator trainerValidator;

  private final TrainerMapper trainerMapper;
  private final TrainingMapper trainingMapper;

  public UserCredentialsDto createTrainer(TrainerCreateDto trainerCreateDto) {
    log.info("Creating Trainer: {}", trainerCreateDto);

    trainerValidator.validate(trainerCreateDto);

    Trainer trainer = trainerMapper.toTrainer(trainerCreateDto);
    User trainerUser = trainer.getUser();

    List<User> users = userRepository.findAllByFirstAndLastNames(
        trainerUser.getFirstName(), trainerUser.getLastName());
    int numOfAppearances = UserUtils.getNumberOfAppearances(users);

    trainerUser.setUsername(UserUtils.buildUsername(trainerUser, numOfAppearances));
    trainerUser.setPassword(UserUtils.generatePassword());

    trainer = trainerRepository.save(trainer);

    return trainerMapper.toUserCredentialsDto(trainer);
  }

  public List<TrainerInfoDto> selectTrainers() {
    log.info("Selecting all Trainers");

    return trainerRepository.findAll().stream()
        .map(trainerMapper::toTrainerInfoDto)
        .toList();
  }

  public TrainerInfoDto selectTrainer(String username) {
    log.info("Selecting Trainer by username: {}", username);

    Trainer trainer = trainerRepository.findByUsername(username)
        .orElseThrow(() -> new TrainerNotFoundException(username));

    return trainerMapper.toTrainerInfoDto(trainer);
  }

  public TrainerInfoDto updateTrainer(TrainerUpdateDto trainerUpdateDto) {
    log.info("Updating Trainer: {}", trainerUpdateDto);

    trainerValidator.validate(trainerUpdateDto);

    Trainer trainer = trainerRepository.findByUsername(trainerUpdateDto.getUsername())
        .orElseThrow(() -> new TrainerNotFoundException(trainerUpdateDto.getUsername()));

    trainerMapper.updateTrainer(trainerUpdateDto, trainer);
    trainer = trainerRepository.update(trainer);

    return trainerMapper.toTrainerInfoDto(trainer);
  }

  public List<TrainingInfoDto> findTrainerTrainings(String username, LocalDate fromDate,
      LocalDate toDate, String traineeName) {
    log.info("""
        Getting Trainer's (username={}) Trainings by next params:
        - fromDate={}
        - toDate={}
        - traineeName={}""", username, fromDate, toDate, traineeName);

    return trainingRepository.findAllByTrainerUsernameAndParams(username, fromDate, toDate, traineeName)
        .stream()
        .map(trainingMapper::toTrainingInfoDto)
        .toList();
  }

  public List<TrainerShortInfoDto> findUnassignedTrainers(String traineeUsername) {
    log.info("Getting unassigned active Trainers on Trainee with username: {}", traineeUsername);

    return trainerRepository.findAllUnassignedByTraineeUsername(traineeUsername)
        .stream()
        .map(trainerMapper::toTrainerShortInfoDto)
        .toList();
  }
}
