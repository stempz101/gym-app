package com.epam.gymapp.mainmicroservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.epam.gymapp.mainmicroservice.model.custom.TrainerWorkload;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.repository.TraineeRepository;
import com.epam.gymapp.mainmicroservice.repository.TrainerRepository;
import com.epam.gymapp.mainmicroservice.repository.TrainingRepository;
import com.epam.gymapp.mainmicroservice.repository.UserRepository;
import com.epam.gymapp.mainmicroservice.test.utils.JwtTokenTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TraineeTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainerTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainingTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.UserTestUtil;
import com.epam.gymapp.mainmicroservice.utils.UserUtils;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {

  @InjectMocks
  private TraineeService traineeService;

  @Mock
  private TraineeRepository traineeRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private TrainingRepository trainingRepository;

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private SessionUserRepository sessionUserRepository;

  @Mock
  private TraineeMapper traineeMapper;

  @Mock
  private TrainingMapper trainingMapper;

  @Mock
  private TrainerMapper trainerMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @Mock
  private TrainerService trainerService;

  @Test
  void createTrainee_Success() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    Trainee mappedTrainee = TraineeTestUtil.getTrainee1FromTraineeCreateDto();
    Trainee createdTrainee = TraineeTestUtil.getTrainee1();
    UserCredentialsDto expectedResult =
        UserTestUtil.getTraineeUserCredentialsDto1WithToken(JwtTokenTestUtil.TEST_JWT_TOKEN);

    try (MockedStatic<UserUtils> userUtils = mockStatic(UserUtils.class)) {
      // When
      when(traineeMapper.toTrainee(any(TraineeCreateDto.class))).thenReturn(mappedTrainee);
      when(userRepository.findAllByFirstAndLastNames(any(), any())).thenReturn(
          Collections.emptyList());
      userUtils.when(() -> UserUtils.getNumberOfAppearances(any())).thenReturn(0);
      userUtils.when(() -> UserUtils.buildUsername(any(), anyInt()))
          .thenReturn(createdTrainee.getUser().getUsername());
      userUtils.when(UserUtils::generatePassword)
          .thenReturn(createdTrainee.getUser().getPassword().toCharArray());
      when(passwordEncoder.encode(any())).thenReturn(createdTrainee.getUser().getPassword());
      when(traineeRepository.save(any())).thenReturn(createdTrainee);
      when(jwtService.generateAndSaveToken(any())).thenReturn(expectedResult.getToken());

      UserCredentialsDto result = traineeService.createTrainee(traineeCreateDto);

      // Then
      verify(traineeMapper, times(1)).toTrainee(any(TraineeCreateDto.class));
      verify(userRepository, times(1)).findAllByFirstAndLastNames(any(), any());
      userUtils.verify(() -> UserUtils.getNumberOfAppearances(any()), times(1));
      userUtils.verify(() -> UserUtils.buildUsername(any(), anyInt()), times(1));
      userUtils.verify(UserUtils::generatePassword, times(1));
      verify(passwordEncoder, times(1)).encode(any());
      verify(traineeRepository, times(1)).save(any());
      verify(jwtService, times(1)).generateAndSaveToken(any());

      assertThat(result, samePropertyValuesAs(expectedResult));
    }
  }

  @Test
  void selectTrainees_Success() {
    // Given
    List<Trainee> trainees = TraineeTestUtil.getTrainees();
    List<TraineeInfoDto> expectedResult = TraineeTestUtil.getTraineeInfoDtos();

    // When
    when(traineeRepository.findAll()).thenReturn(trainees);
    when(traineeMapper.toTraineeInfoDto(any())).thenReturn(expectedResult.get(0),
        expectedResult.get(1), expectedResult.get(2));

    List<TraineeInfoDto> result = traineeService.selectTrainees();

    // Then
    verify(traineeRepository, times(1)).findAll();
    verify(traineeMapper, times(3)).toTraineeInfoDto(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TraineeTestUtil.getTraineeInfoDto1(),
        TraineeTestUtil.getTraineeInfoDto2(),
        TraineeTestUtil.getTraineeInfoDto3()
    ));
  }

  @Test
  void selectTrainee_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    when(traineeMapper.toTraineeInfoDto(any())).thenReturn(expectedResult);

    TraineeInfoDto result = traineeService.selectTrainee(trainee.getUser().getUsername());

    // Then
    verify(traineeRepository, times(1)).findByUsername(any());
    verify(traineeMapper, times(1)).toTraineeInfoDto(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectTrainee_TraineeNotFound_Failure() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(TraineeNotFoundException.class,
        () -> traineeService.selectTrainee(trainee.getUser().getUsername()));
  }

  @Test
  void updateTrainee_Success() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    Trainee trainee = TraineeTestUtil.getTrainee1();
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1AfterUpdate();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    doNothing().when(traineeMapper).updateTrainee(any(), any());
    when(traineeRepository.save(any())).thenReturn(trainee);
    when(traineeMapper.toTraineeInfoDtoAfterUpdate(any())).thenReturn(expectedResult);

    TraineeInfoDto result = traineeService.updateTrainee(traineeUpdateDto);

    // Then
    verify(traineeRepository, times(1)).findByUsername(any());
    verify(traineeMapper, times(1)).updateTrainee(any(), any());
    verify(traineeRepository, times(1)).save(any());
    verify(traineeMapper, times(1)).toTraineeInfoDtoAfterUpdate(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void updateTrainee_TraineeNotFound_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(TraineeNotFoundException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void deleteTrainee_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();
    TrainerWorkload trainerWorkload = TrainerTestUtil.getTrainerWorkload1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    when(trainerRepository.findTrainersWorkloadByTraineeUsernameAfterDate(any(), any()))
        .thenReturn(Collections.singletonList(trainerWorkload));
    doNothing().when(trainingRepository).deleteByTraineeId(any());
    doNothing().when(traineeRepository).delete(any());
    doNothing().when(trainerService).updateTrainerWorkload(any());
    doNothing().when(sessionUserRepository).deleteByUsername(any());

    traineeService.deleteTrainee(trainee.getUser().getUsername());

    // Then
    verify(traineeRepository, times(1)).findByUsername(any());
    verify(trainerRepository, times(1))
        .findTrainersWorkloadByTraineeUsernameAfterDate(any(), any());
    verify(trainingRepository, times(1)).deleteByTraineeId(any());
    verify(traineeRepository, times(1)).delete(any());
    verify(trainerService, times(1)).updateTrainerWorkload(any());
    verify(sessionUserRepository, times(1)).deleteByUsername(any());
  }

  @Test
  void deleteTrainee_TraineeNotFound_Failure() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(TraineeNotFoundException.class,
        () -> traineeService.deleteTrainee(trainee.getUser().getUsername()));
  }

  @Test
  void findTraineeTrainings_Success() {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    LocalDate fromDate = LocalDate.of(2024, 2, 5);
    LocalDate toDate = LocalDate.of(2024, 2, 20);
    List<Training> trainings = TrainingTestUtil.getTrainingsOfTrainee2();
    List<TrainingInfoDto> expectedResult = TrainingTestUtil.getTrainingInfoDtosOfTrainee2();

    // When
    when(trainingRepository.findAllByTraineeUsernameAndParams(any(), any(), any(), any(), any()))
        .thenReturn(trainings);
    when(trainingMapper.toTrainingInfoDto(any()))
        .thenReturn(expectedResult.get(0), expectedResult.get(1));

    List<TrainingInfoDto> result = traineeService
        .findTraineeTrainings(traineeUsername, fromDate, toDate, null, null);

    // Then
    verify(trainingRepository, times(1))
        .findAllByTraineeUsernameAndParams(any(), any(), any(), any(), any());
    verify(trainingMapper, times(2)).toTrainingInfoDto(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTrainingInfoDto2(),
        TrainingTestUtil.getTrainingInfoDto5()
    ));
  }

  @Test
  void updateTrainerList_Success() {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    Trainee trainee = TraineeTestUtil.getTrainee2();
    List<Trainer> trainers = TraineeTestUtil.getTraineeUpdatedTrainersList2();
    List<TrainerShortInfoDto> expectedResult = TraineeTestUtil.getTraineeUpdatedTrainerShortInfoDtosList2();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    when(trainerRepository.findAllByUsernameIn(any())).thenReturn(trainers);
    when(traineeRepository.save(any())).thenReturn(trainee);
    when(trainerMapper.toTrainerShortInfoDto(any()))
        .thenReturn(expectedResult.get(0), expectedResult.get(1));

    List<TrainerShortInfoDto> result = traineeService.updateTrainerList(traineeTrainersUpdateDto);

    // Then
    verify(traineeRepository, times(1)).findByUsername(any());
    verify(trainerRepository, times(1)).findAllByUsernameIn(any());
    verify(traineeRepository, times(1)).save(any());
    verify(trainerMapper, times(2)).toTrainerShortInfoDto(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainerShortInfoDto1(),
        TrainerTestUtil.getTrainerShortInfoDto2()
    ));
  }

  @Test
  void updateTrainerList_TraineeNotFound_Failure() {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTraineeUsername("some_username");

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(TraineeNotFoundException.class,
        () -> traineeService.updateTrainerList(traineeTrainersUpdateDto));
  }

  @Test
  void updateTrainerList_FoundTrainerListIsNull_Failure() {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    Trainee trainee = TraineeTestUtil.getTrainee2();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    when(trainerRepository.findAllByUsernameIn(any())).thenReturn(null);

    // Then
    assertThrows(TrainerNotFoundException.class,
        () -> traineeService.updateTrainerList(traineeTrainersUpdateDto));
  }

  @Test
  void updateTrainerList_FoundTrainerListIsEmpty_Failure() {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    Trainee trainee = TraineeTestUtil.getTrainee2();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    when(trainerRepository.findAllByUsernameIn(any())).thenReturn(Collections.emptyList());

    // Then
    assertThrows(TrainerNotFoundException.class,
        () -> traineeService.updateTrainerList(traineeTrainersUpdateDto));
  }
}
