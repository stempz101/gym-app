package com.epam.gymapp.mainmicroservice.service;

import com.epam.gymapp.mainmicroservice.dto.TrainerCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.dto.UserCredentialsDto;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.mapper.TrainerMapper;
import com.epam.gymapp.mainmicroservice.mapper.TrainingMapper;
import com.epam.gymapp.mainmicroservice.model.Trainer;
import com.epam.gymapp.mainmicroservice.model.User;
import com.epam.gymapp.mainmicroservice.producer.ReportsProducer;
import com.epam.gymapp.mainmicroservice.repository.TrainerRepository;
import com.epam.gymapp.mainmicroservice.repository.TrainingRepository;
import com.epam.gymapp.mainmicroservice.repository.TrainingTypeRepository;
import com.epam.gymapp.mainmicroservice.repository.UserRepository;
import com.epam.gymapp.mainmicroservice.utils.UserUtils;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainerService {

  private static final Logger log = LoggerFactory.getLogger(TrainerService.class);

  private final TrainerRepository trainerRepository;
  private final UserRepository userRepository;
  private final TrainingRepository trainingRepository;
  private final TrainingTypeRepository trainingTypeRepository;

  private final TrainerMapper trainerMapper;
  private final TrainingMapper trainingMapper;

  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final ReportsProducer reportsProducer;

  @Transactional
  public UserCredentialsDto createTrainer(TrainerCreateDto trainerCreateDto) {
    log.info("Creating Trainer: {}", trainerCreateDto);

    Trainer trainer = trainerMapper.toTrainer(trainerCreateDto);
    User trainerUser = trainer.getUser();

    List<User> users = userRepository.findAllByFirstAndLastNames(
        trainerUser.getFirstName(), trainerUser.getLastName());
    int numOfAppearances = UserUtils.getNumberOfAppearances(users);

    trainerUser.setUsername(UserUtils.buildUsername(trainerUser, numOfAppearances));
    char[] password = UserUtils.generatePassword();
    trainerUser.setPassword(passwordEncoder.encode(String.valueOf(password)));

    setSpecializationIfExists(trainer);

    trainer = trainerRepository.save(trainer);
    String token = jwtService.generateAndSaveToken(trainer.getUser());

    return new UserCredentialsDto(trainer.getUser().getUsername(), password, token);
  }

  @Transactional(readOnly = true)
  public List<TrainerInfoDto> selectTrainers() {
    log.info("Selecting all Trainers");

    return trainerRepository.findAll().stream()
        .map(trainerMapper::toTrainerInfoDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public TrainerInfoDto selectTrainer(String username) {
    log.info("Selecting Trainer by username: {}", username);

    Trainer trainer = trainerRepository.findByUsername(username)
        .orElseThrow(() -> new TrainerNotFoundException(username));

    return trainerMapper.toTrainerInfoDto(trainer);
  }

  @Transactional
  public TrainerInfoDto updateTrainer(TrainerUpdateDto trainerUpdateDto) {
    log.info("Updating Trainer: {}", trainerUpdateDto);

    Trainer trainer = trainerRepository.findByUsername(trainerUpdateDto.getUsername())
        .orElseThrow(() -> new TrainerNotFoundException(trainerUpdateDto.getUsername()));

    trainerMapper.updateTrainer(trainerUpdateDto, trainer);
    trainer = trainerRepository.save(trainer);

    return trainerMapper.toTrainerInfoDtoAfterUpdate(trainer);
  }

  @Transactional(readOnly = true)
  public List<TrainingInfoDto> findTrainerTrainings(
      String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
    log.info("""
        Getting Trainer's (username={}) Trainings by next params:
        - fromDate={}
        - toDate={}
        - traineeName={}""", username, fromDate, toDate, traineeName);

    return trainingRepository.findAllByTrainerUsernameAndParams(username, fromDate, toDate,
            traineeName)
        .stream()
        .map(trainingMapper::toTrainingInfoDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<TrainerShortInfoDto> findUnassignedTrainers(String traineeUsername) {
    log.info("Getting unassigned active Trainers on Trainee with username: {}", traineeUsername);

    return trainerRepository.findAllUnassignedByTraineeUsername(traineeUsername)
        .stream()
        .map(trainerMapper::toTrainerShortInfoDto)
        .toList();
  }

  private void setSpecializationIfExists(Trainer trainer) {
    trainingTypeRepository.findByName(trainer.getSpecialization().getName())
        .ifPresent(trainer::setSpecialization);
  }

  public List<TrainerWorkloadDto> retrieveTrainersWorkloadForMonth(
      int year, int month, String firstName, String lastName) {

    log.debug("Sending a request to retrieve trainers' workload for certain month: {}, {}",
        year, Month.of(month));

    return reportsProducer.retrieveTrainersWorkloadForMonth(year, month, firstName, lastName);
  }
}
