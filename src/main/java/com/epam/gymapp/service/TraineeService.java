package com.epam.gymapp.service;

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.mapper.TraineeMapper;
import com.epam.gymapp.mapper.TrainerMapper;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.model.User;
import com.epam.gymapp.repository.TraineeRepository;
import com.epam.gymapp.repository.TrainerRepository;
import com.epam.gymapp.repository.TrainingRepository;
import com.epam.gymapp.repository.UserRepository;
import com.epam.gymapp.utils.UserUtils;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TraineeService {

  private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

  private final TraineeRepository traineeRepository;
  private final UserRepository userRepository;
  private final TrainingRepository trainingRepository;
  private final TrainerRepository trainerRepository;

  private final TraineeMapper traineeMapper;
  private final TrainingMapper trainingMapper;
  private final TrainerMapper trainerMapper;

  public UserCredentialsDto createTrainee(TraineeCreateDto traineeCreateDto) {
    log.info("Creating Trainee: {}", traineeCreateDto);

    Trainee trainee = traineeMapper.toTrainee(traineeCreateDto);
    User traineeUser = trainee.getUser();

    List<User> users = userRepository.findAllByFirstAndLastNames(
        traineeUser.getFirstName(), traineeUser.getLastName());
    int numOfAppearances = UserUtils.getNumberOfAppearances(users);

    traineeUser.setUsername(UserUtils.buildUsername(traineeUser, numOfAppearances));
    traineeUser.setPassword(UserUtils.generatePassword());

    trainee = traineeRepository.save(trainee);

    return traineeMapper.toUserCredentialsDto(trainee);
  }

  public List<TraineeInfoDto> selectTrainees() {
    log.info("Selecting all Trainees");

    return traineeRepository.findAll().stream()
        .map(traineeMapper::toTraineeInfoDto)
        .toList();
  }

  public TraineeInfoDto selectTrainee(String username) {
    log.info("Selecting Trainee by username: {}", username);

    Trainee trainee = traineeRepository.findByUsername(username)
        .orElseThrow(() -> new TraineeNotFoundException(username));

    return traineeMapper.toTraineeInfoDto(trainee);
  }

  public TraineeInfoDto updateTrainee(TraineeUpdateDto traineeUpdateDto) {
    log.info("Updating Trainee: {}", traineeUpdateDto);

    Trainee trainee = traineeRepository.findByUsername(traineeUpdateDto.getUsername())
        .orElseThrow(() -> new TraineeNotFoundException(traineeUpdateDto.getUsername()));

    traineeMapper.updateTrainee(traineeUpdateDto, trainee);
    trainee = traineeRepository.update(trainee);

    return traineeMapper.toTraineeInfoDtoAfterUpdate(trainee);
  }

  public void deleteTrainee(String username) {
    log.info("Deleting Trainee by username: {}", username);

    Trainee trainee = traineeRepository.findByUsername(username)
        .orElseThrow(() -> new TraineeNotFoundException(username));

    traineeRepository.delete(trainee);
  }

  public List<TrainingInfoDto> findTraineeTrainings(String username, LocalDate fromDate,
      LocalDate toDate, String trainerName, String trainingType) {
    log.info("""
        Getting Trainee's (username={}) Trainings by next params:
        - fromDate={}
        - toDate={}
        - trainerName={}
        - trainingType={}""", username, fromDate, toDate, trainerName, trainingType);

    List<Training> allByTraineeUsernameAndParams = trainingRepository.findAllByTraineeUsernameAndParams(
        username, fromDate, toDate,
        trainerName, trainingType);
    return allByTraineeUsernameAndParams
        .stream()
        .map(trainingMapper::toTrainingInfoDto)
        .toList();
  }

  public List<TrainerShortInfoDto> updateTrainerList(TraineeTrainersUpdateDto traineeTrainersUpdateDto) {
    log.info("Updating Trainee's (username={}) Trainer list: {}",
        traineeTrainersUpdateDto.getTraineeUsername(), traineeTrainersUpdateDto.getTrainerUsernames());

    Trainee trainee = traineeRepository.findByUsername(traineeTrainersUpdateDto.getTraineeUsername())
        .orElseThrow(() -> new TraineeNotFoundException(traineeTrainersUpdateDto.getTraineeUsername()));

    List<Trainer> trainers = trainerRepository.findAllByUsernameIn(
        traineeTrainersUpdateDto.getTrainerUsernames());
    checkForMissingTrainerUsernames(traineeTrainersUpdateDto.getTrainerUsernames(), trainers);

    trainee.setTrainers(trainers);
    trainee = traineeRepository.update(trainee);

    return trainee.getTrainers().stream()
        .map(trainerMapper::toTrainerShortInfoDto)
        .toList();
  }

  private void checkForMissingTrainerUsernames(List<String> usernames, List<Trainer> trainers) {
    List<String> missingUsernames = searchMissingTrainerUsernames(usernames, trainers);
    if (!missingUsernames.isEmpty()) {
      throw new TrainerNotFoundException(missingUsernames);
    }
  }

  private List<String> searchMissingTrainerUsernames(List<String> usernames, List<Trainer> trainers) {
    if (trainers == null || trainers.isEmpty()) {
      return usernames;
    }

    return usernames.stream()
        .filter(username -> trainers.stream()
            .map(trainer -> trainer.getUser().getUsername())
            .noneMatch(username::equals))
        .toList();
  }
}
