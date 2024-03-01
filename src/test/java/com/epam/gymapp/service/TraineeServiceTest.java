package com.epam.gymapp.service;

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

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TraineeValidationException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.mapper.TraineeMapper;
import com.epam.gymapp.mapper.TrainerMapper;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.repository.TraineeRepository;
import com.epam.gymapp.repository.TrainerRepository;
import com.epam.gymapp.repository.TrainingRepository;
import com.epam.gymapp.repository.UserRepository;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import com.epam.gymapp.utils.UserUtils;
import com.epam.gymapp.validator.TraineeValidator;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

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

  @Spy
  private TraineeValidator traineeValidator;

  @Mock
  private TraineeMapper traineeMapper;

  @Mock
  private TrainingMapper trainingMapper;

  @Mock
  private TrainerMapper trainerMapper;

  @Test
  void createTrainee_Success() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    Trainee mappedTrainee = TraineeTestUtil.getTrainee1FromTraineeCreateDto();
    Trainee createdTrainee = TraineeTestUtil.getTrainee1();
    UserCredentialsDto expectedResult = UserTestUtil.getTraineeUserCredentialsDto1();

    try (MockedStatic<UserUtils> userUtils = mockStatic(UserUtils.class)) {
      // When
      when(traineeMapper.toTrainee(any(TraineeCreateDto.class))).thenReturn(mappedTrainee);
      when(userRepository.findAllByFirstAndLastNames(any(), any())).thenReturn(
          Collections.emptyList());
      userUtils.when(() -> UserUtils.getNumberOfAppearances(any())).thenReturn(0);
      userUtils.when(() -> UserUtils.buildUsername(any(), anyInt()))
          .thenReturn(createdTrainee.getUser().getUsername());
      userUtils.when(UserUtils::generatePassword)
          .thenReturn(createdTrainee.getUser().getPassword());
      when(traineeRepository.save(any())).thenReturn(createdTrainee);
      when(traineeMapper.toUserCredentialsDto(any())).thenReturn(expectedResult);

      UserCredentialsDto result = traineeService.createTrainee(traineeCreateDto);

      // Then
      verify(traineeValidator, times(1)).validate(any(TraineeCreateDto.class));
      verify(traineeMapper, times(1)).toTrainee(any(TraineeCreateDto.class));
      verify(userRepository, times(1)).findAllByFirstAndLastNames(any(), any());
      userUtils.verify(() -> UserUtils.getNumberOfAppearances(any()), times(1));
      userUtils.verify(() -> UserUtils.buildUsername(any(), anyInt()), times(1));
      userUtils.verify(UserUtils::generatePassword, times(1));
      verify(traineeRepository, times(1)).save(any());
      verify(traineeMapper, times(1)).toUserCredentialsDto(any());

      assertThat(result, samePropertyValuesAs(expectedResult));
    }
  }

  @Test
  void createTrainee_TraineeCreateDtoIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> traineeService.createTrainee(null));
  }

  @Test
  void createTrainee_FirstNameIsNull_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName(null);

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.createTrainee(traineeCreateDto));
  }

  @Test
  void createTrainee_FirstNameIsEmpty_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName("");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.createTrainee(traineeCreateDto));
  }

  @Test
  void createTrainee_FirstNameIsBlank_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName("   ");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.createTrainee(traineeCreateDto));
  }

  @Test
  void createTrainee_LastNameIsNull_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName(null);

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.createTrainee(traineeCreateDto));
  }

  @Test
  void createTrainee_LastNameIsEmpty_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName("");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.createTrainee(traineeCreateDto));
  }

  @Test
  void createTrainee_LastNameIsBlank_Failure() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName("   ");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.createTrainee(traineeCreateDto));
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
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    doNothing().when(traineeMapper).updateTrainee(any(), any());
    when(traineeRepository.update(any())).thenReturn(trainee);
    when(traineeMapper.toTraineeInfoDto(any())).thenReturn(expectedResult);

    TraineeInfoDto result = traineeService.updateTrainee(traineeUpdateDto);

    // Then
    verify(traineeValidator, times(1)).validate(any(TraineeUpdateDto.class));
    verify(traineeRepository, times(1)).findByUsername(any());
    verify(traineeMapper, times(1)).updateTrainee(any(), any());
    verify(traineeRepository, times(1)).update(any());
    verify(traineeMapper, times(1)).toTraineeInfoDto(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void updateTrainee_TraineeIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> traineeService.updateTrainee(null));
  }

  @Test
  void updateTrainee_UsernameIsNull_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setUsername(null);

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void updateTrainee_UsernameIsEmpty_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setUsername("");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void updateTrainee_UsernameIsBlank_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setUsername("   ");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void updateTrainee_FirstNameIsNull_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setFirstName(null);

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void updateTrainee_FirstNameIsEmpty_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setFirstName("");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void updateTrainee_FirstNameIsBlank_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setFirstName("   ");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void updateTrainee_LastNameIsNull_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setLastName(null);

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void updateTrainee_LastNameIsEmpty_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setLastName("");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
  }

  @Test
  void updateTrainee_LastNameIsBlank_Failure() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setLastName("   ");

    // When & Then
    assertThrows(TraineeValidationException.class,
        () -> traineeService.updateTrainee(traineeUpdateDto));
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

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    doNothing().when(traineeRepository).delete(any());

    traineeService.deleteTrainee(trainee.getUser().getUsername());

    // Then
    verify(traineeRepository, times(1)).findByUsername(any());
    verify(traineeRepository, times(1)).delete(any());
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
    when(traineeRepository.update(any())).thenReturn(trainee);
    when(trainerMapper.toTrainerShortInfoDto(any()))
        .thenReturn(expectedResult.get(0), expectedResult.get(1));

    List<TrainerShortInfoDto> result = traineeService.updateTrainerList(traineeTrainersUpdateDto);

    // Then
    verify(traineeRepository, times(1)).findByUsername(any());
    verify(trainerRepository, times(1)).findAllByUsernameIn(any());
    verify(traineeRepository, times(1)).update(any());
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
