package com.epam.gymapp.mainmicroservice.service;

import com.epam.gymapp.mainmicroservice.dto.TraineeCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.dto.UserCredentialsDto;
import com.epam.gymapp.mainmicroservice.exception.TraineeNotFoundException;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.mapper.TraineeMapper;
import com.epam.gymapp.mainmicroservice.mapper.TrainerMapper;
import com.epam.gymapp.mainmicroservice.mapper.TrainingMapper;
import com.epam.gymapp.mainmicroservice.model.Trainee;
import com.epam.gymapp.mainmicroservice.model.Trainer;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.mainmicroservice.model.User;
import com.epam.gymapp.mainmicroservice.producer.ReportsProducer;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.repository.TraineeRepository;
import com.epam.gymapp.mainmicroservice.repository.TrainerRepository;
import com.epam.gymapp.mainmicroservice.repository.TrainingRepository;
import com.epam.gymapp.mainmicroservice.repository.UserRepository;
import com.epam.gymapp.mainmicroservice.utils.UserUtils;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TraineeService {

  private static final Logger log = LoggerFactory.getLogger(TraineeService.class);

  private final TraineeRepository traineeRepository;
  private final UserRepository userRepository;
  private final TrainingRepository trainingRepository;
  private final TrainerRepository trainerRepository;
  private final SessionUserRepository sessionUserRepository;

  private final TraineeMapper traineeMapper;
  private final TrainingMapper trainingMapper;
  private final TrainerMapper trainerMapper;

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final ReportsProducer reportsProducer;

  @Transactional
  public UserCredentialsDto createTrainee(TraineeCreateDto traineeCreateDto) {
    log.info("Creating Trainee: {}", traineeCreateDto);

    Trainee trainee = traineeMapper.toTrainee(traineeCreateDto);
    User traineeUser = trainee.getUser();

    List<User> users = userRepository.findAllByFirstAndLastNames(
        traineeUser.getFirstName(), traineeUser.getLastName());
    int numOfAppearances = UserUtils.getNumberOfAppearances(users);

    traineeUser.setUsername(UserUtils.buildUsername(traineeUser, numOfAppearances));
    char[] password = UserUtils.generatePassword();
    traineeUser.setPassword(passwordEncoder.encode(String.valueOf(password)));

    trainee = traineeRepository.save(trainee);
    String token = jwtService.generateAndSaveToken(trainee.getUser());

    return new UserCredentialsDto(trainee.getUser().getUsername(), password, token);
  }

  @Transactional(readOnly = true)
  public List<TraineeInfoDto> selectTrainees() {
    log.info("Selecting all Trainees");

    return traineeRepository.findAll().stream()
        .map(traineeMapper::toTraineeInfoDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public TraineeInfoDto selectTrainee(String username) {
    log.info("Selecting Trainee by username: {}", username);

    Trainee trainee = traineeRepository.findByUsername(username)
        .orElseThrow(() -> new TraineeNotFoundException(username));

    return traineeMapper.toTraineeInfoDto(trainee);
  }

  @Transactional
  public TraineeInfoDto updateTrainee(TraineeUpdateDto traineeUpdateDto) {
    log.info("Updating Trainee: {}", traineeUpdateDto);

    Trainee trainee = traineeRepository.findByUsername(traineeUpdateDto.getUsername())
        .orElseThrow(() -> new TraineeNotFoundException(traineeUpdateDto.getUsername()));

    traineeMapper.updateTrainee(traineeUpdateDto, trainee);
    trainee = traineeRepository.save(trainee);

    return traineeMapper.toTraineeInfoDtoAfterUpdate(trainee);
  }

  @Transactional
  public void deleteTrainee(String username) {
    log.info("Deleting Trainee by username: {}", username);

    Trainee trainee = traineeRepository.findByUsername(username)
        .orElseThrow(() -> new TraineeNotFoundException(username));
    List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos = trainerRepository
        .findTrainersWorkloadByTraineeUsernameAfterDate(username, LocalDate.now())
        .stream()
        .map(trainerRecord -> trainerMapper.toTrainerWorkloadUpdateDto(trainerRecord, ActionType.DELETE))
        .toList();

    trainingRepository.deleteByTraineeId(trainee.getId());
    traineeRepository.delete(trainee);

    if (!trainerWorkloadUpdateDtos.isEmpty()) {
      reportsProducer.updateTrainerWorkload(trainerWorkloadUpdateDtos);
    }

    sessionUserRepository.deleteByUsername(trainee.getUser().getUsername());
  }

  @Transactional(readOnly = true)
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

  @Transactional
  public List<TrainerShortInfoDto> updateTrainerList(TraineeTrainersUpdateDto traineeTrainersUpdateDto) {
    log.info("Updating Trainee's (username={}) Trainer list: {}",
        traineeTrainersUpdateDto.getTraineeUsername(), traineeTrainersUpdateDto.getTrainerUsernames());

    Trainee trainee = traineeRepository.findByUsername(traineeTrainersUpdateDto.getTraineeUsername())
        .orElseThrow(() -> new TraineeNotFoundException(traineeTrainersUpdateDto.getTraineeUsername()));

    List<Trainer> trainers = trainerRepository.findAllByUsernameIn(
        traineeTrainersUpdateDto.getTrainerUsernames());
    checkForMissingTrainerUsernames(traineeTrainersUpdateDto.getTrainerUsernames(), trainers);

    trainee.setTrainers(trainers);
    trainee = traineeRepository.save(trainee);

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
