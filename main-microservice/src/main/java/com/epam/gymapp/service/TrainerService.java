package com.epam.gymapp.service;

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainerWorkloadDto;
import com.epam.gymapp.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.TrainerWorkingHoursUpdateException;
import com.epam.gymapp.mapper.TrainerMapper;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.model.User;
import com.epam.gymapp.repository.TrainerRepository;
import com.epam.gymapp.repository.TrainingRepository;
import com.epam.gymapp.repository.TrainingTypeRepository;
import com.epam.gymapp.repository.UserRepository;
import com.epam.gymapp.utils.UserUtils;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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
  private final HttpServletRequest httpServletRequest;

  private WebClient.Builder webClientBuilder;

  @Value("${application.reports-microservice.base-url}")
  private String reportsMicroserviceBaseUrl;

  @Value("${application.logging.transaction-id.key}")
  private String transactionIdKey;

  @Value("${application.logging.transaction-id.header}")
  private String transactionIdHeader;

  @Autowired
  public void setWebClientBuilder(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

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

  @Retry(name = "reportsMicroserviceRetry")
  @CircuitBreaker(name = "reportsMicroserviceCircuitBreaker",
      fallbackMethod = "retrieveTrainersWorkloadForMonthFailure")
  public List<TrainerWorkloadDto> retrieveTrainersWorkloadForMonth(int year, int month, String username) {
    log.debug("Sending a request to retrieve trainers' workload for certain month: {}, {}",
        year, Month.of(month));

    return webClientBuilder.baseUrl(reportsMicroserviceBaseUrl)
        .build()
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/trainer-workload")
            .queryParam("year", year)
            .queryParam("month", month)
            .queryParamIfPresent("username", Optional.ofNullable(username))
            .build())
        .header(transactionIdHeader, MDC.get(transactionIdKey))
        .header(HttpHeaders.AUTHORIZATION, httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION))
        .retrieve()
        .bodyToFlux(TrainerWorkloadDto.class)
        .collectList()
        .block();
  }

  public List<TrainerWorkloadDto> retrieveTrainersWorkloadForMonthFailure(
      int year, int month, String username, Exception ex) {
    return Collections.singletonList(TrainerWorkloadDto.getFallbackObject(year, month, username));
  }

  @Retry(name = "reportsMicroserviceRetry")
  @CircuitBreaker(name = "reportsMicroserviceCircuitBreaker",
      fallbackMethod = "updateTrainerWorkloadFailure")
  public void updateTrainerWorkload(Training training, ActionType actionType) {
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto =
        trainerMapper.toTrainerWorkloadUpdateDto(training, actionType);

    updateTrainerWorkload(Collections.singletonList(trainerWorkloadUpdateDto));
  }

  @Retry(name = "reportsMicroserviceRetry")
  @CircuitBreaker(name = "reportsMicroserviceCircuitBreaker",
      fallbackMethod = "updateTrainerWorkloadFailure")
  public void updateTrainerWorkload(List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos) {
    log.debug("Sending a request to update trainers' workload");

    webClientBuilder.baseUrl(reportsMicroserviceBaseUrl)
        .build()
        .put()
        .uri("/api/trainer-workload")
        .header(transactionIdHeader, MDC.get(transactionIdKey))
        .header(HttpHeaders.AUTHORIZATION, httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION))
        .body(BodyInserters.fromValue(trainerWorkloadUpdateDtos))
        .retrieve()
        .toBodilessEntity()
        .block();
  }

  public void updateTrainerWorkloadFailure(Exception ex) {
    throw new TrainerWorkingHoursUpdateException();
  }
}
