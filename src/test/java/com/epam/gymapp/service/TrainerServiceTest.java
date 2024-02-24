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

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.TrainerValidationException;
import com.epam.gymapp.mapper.TrainerMapper;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.repository.TrainerRepository;
import com.epam.gymapp.repository.TrainingRepository;
import com.epam.gymapp.repository.UserRepository;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import com.epam.gymapp.utils.UserUtils;
import com.epam.gymapp.validator.TrainerValidator;
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
public class TrainerServiceTest {

  @InjectMocks
  private TrainerService trainerService;

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private TrainingRepository trainingRepository;

  @Spy
  private TrainerValidator trainerValidator;

  @Mock
  private TrainerMapper trainerMapper;

  @Mock
  private TrainingMapper trainingMapper;

  @Test
  void createTrainer_Success() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    Trainer mappedTrainer = TrainerTestUtil.getTrainer1FromTrainerCreateDto();
    Trainer createdTrainer = TrainerTestUtil.getTrainer1();
    UserCredentialsDto expectedResult = UserTestUtil.getTrainerUserCredentialsDto1();

    try (MockedStatic<UserUtils> userUtils = mockStatic(UserUtils.class)) {
      // When
      when(trainerMapper.toTrainer(any())).thenReturn(mappedTrainer);
      when(userRepository.findAllByFirstAndLastNames(any(), any())).thenReturn(
          Collections.emptyList());
      userUtils.when(() -> UserUtils.getNumberOfAppearances(any())).thenReturn(0);
      userUtils.when(() -> UserUtils.buildUsername(any(), anyInt()))
          .thenReturn(createdTrainer.getUser().getUsername());
      userUtils.when(UserUtils::generatePassword)
          .thenReturn(createdTrainer.getUser().getPassword());
      when(trainerRepository.save(any())).thenReturn(createdTrainer);
      when(trainerMapper.toUserCredentialsDto(any())).thenReturn(expectedResult);

      UserCredentialsDto result = trainerService.createTrainer(trainerCreateDto);

      // Then
      verify(trainerValidator, times(1)).validate(any(TrainerCreateDto.class));
      verify(trainerMapper, times(1)).toTrainer(any());
      verify(userRepository, times(1)).findAllByFirstAndLastNames(any(), any());
      userUtils.verify(() -> UserUtils.getNumberOfAppearances(any()), times(1));
      userUtils.verify(() -> UserUtils.buildUsername(any(), anyInt()), times(1));
      userUtils.verify(UserUtils::generatePassword, times(1));
      verify(trainerRepository, times(1)).save(any());
      verify(trainerMapper, times(1)).toUserCredentialsDto(any());

      assertThat(result, samePropertyValuesAs(expectedResult));
    }
  }

  @Test
  void createTrainer_TrainerCreateDtoIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer(null));
  }

  @Test
  void createTrainer_FirstNameIsNull_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName(null);

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void createTrainer_FirstNameIsEmpty_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName("");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void createTrainer_FirstNameIsBlank_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName("   ");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void createTrainer_LastNameIsNull_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName(null);

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void createTrainer_LastNameIsEmpty_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName("");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void createTrainer_LastNameIsBlank_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName("   ");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void createTrainer_SpecializationIsNull_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization(null);

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void createTrainer_SpecializationIsEmpty_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization("");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void createTrainer_SpecializationIsBlank_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization("   ");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.createTrainer(trainerCreateDto));
  }

  @Test
  void selectTrainers_Success() {
    // Given
    List<Trainer> trainers = TrainerTestUtil.getTrainers();
    List<TrainerInfoDto> expectedResult = TrainerTestUtil.getTrainerInfoDtos();

    // When
    when(trainerRepository.findAll()).thenReturn(trainers);
    when(trainerMapper.toTrainerInfoDto(any())).thenReturn(expectedResult.get(0),
        expectedResult.get(1), expectedResult.get(2));

    List<TrainerInfoDto> result = trainerService.selectTrainers();

    // Then
    verify(trainerRepository, times(1)).findAll();
    verify(trainerMapper, times(3)).toTrainerInfoDto(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainerInfoDto1(),
        TrainerTestUtil.getTrainerInfoDto2(),
        TrainerTestUtil.getTrainerInfoDto3()
    ));
  }

  @Test
  void selectTrainer_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();
    TrainerInfoDto expectedResult = TrainerTestUtil.getTrainerInfoDto1();

    // When
    when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(trainer));
    when(trainerMapper.toTrainerInfoDto(any())).thenReturn(expectedResult);

    TrainerInfoDto result = trainerService.selectTrainer(trainer.getUser().getUsername());

    // Then
    verify(trainerRepository, times(1)).findByUsername(any());
    verify(trainerMapper, times(1)).toTrainerInfoDto(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectTrainer_TrainerNotFound_Failure() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();

    // When
    when(trainerRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(TrainerNotFoundException.class,
        () -> trainerService.selectTrainer(trainer.getUser().getUsername()));
  }

  @Test
  void updateTrainer_Success() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    Trainer trainer = TrainerTestUtil.getTrainer1();
    TrainerInfoDto expectedResult = TrainerTestUtil.getTrainerInfoDto1();

    // When
    when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(trainer));
    doNothing().when(trainerMapper).updateTrainer(any(), any());
    when(trainerRepository.update(any())).thenReturn(trainer);
    when(trainerMapper.toTrainerInfoDto(any())).thenReturn(expectedResult);

    TrainerInfoDto result = trainerService.updateTrainer(trainerUpdateDto);

    // Then
    verify(trainerValidator, times(1)).validate(any(TrainerUpdateDto.class));
    verify(trainerRepository, times(1)).findByUsername(any());
    verify(trainerMapper, times(1)).updateTrainer(any(), any());
    verify(trainerRepository, times(1)).update(any());
    verify(trainerMapper, times(1)).toTrainerInfoDto(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void updateTrainee_TraineeIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> trainerService.updateTrainer(null));
  }

  @Test
  void updateTrainer_UsernameIsNull_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setUsername(null);

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_UsernameIsEmpty_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setUsername("");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_UsernameIsBlank_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setUsername("   ");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_FirstNameIsNull_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setFirstName(null);

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_FirstNameIsEmpty_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setFirstName("");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_FirstNameIsBlank_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setFirstName("   ");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_LastNameIsNull_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setLastName(null);

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_LastNameIsEmpty_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setLastName("");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_LastNameIsBlank_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setLastName("   ");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_SpecializationIsNull_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setSpecialization(null);

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_SpecializationIsEmpty_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setSpecialization("");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_SpecializationIsBlank_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setSpecialization("   ");

    // When & Then
    assertThrows(TrainerValidationException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void updateTrainer_TrainerNotFound_Failure() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();

    // When
    when(trainerRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(TrainerNotFoundException.class,
        () -> trainerService.updateTrainer(trainerUpdateDto));
  }

  @Test
  void findTrainerTrainings_Success() {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    LocalDate fromDate = LocalDate.of(2024, 2, 5);
    LocalDate toDate = LocalDate.of(2024, 2, 20);
    List<Training> trainings = TrainingTestUtil.getTrainingsOfTrainer2();
    List<TrainingInfoDto> expectedResult = TrainingTestUtil.getTrainingInfoDtosOfTrainer2();

    // When
    when(trainingRepository.findAllByTrainerUsernameAndParams(any(), any(), any(), any()))
        .thenReturn(trainings);
    when(trainingMapper.toTrainingInfoDto(any()))
        .thenReturn(expectedResult.get(0), expectedResult.get(1));

    List<TrainingInfoDto> result = trainerService
        .findTrainerTrainings(trainerUsername, fromDate, toDate, null);

    // Then
    verify(trainingRepository, times(1))
        .findAllByTrainerUsernameAndParams(any(), any(), any(), any());
    verify(trainingMapper, times(2)).toTrainingInfoDto(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTrainingInfoDto1(),
        TrainingTestUtil.getTrainingInfoDto6()
    ));
  }

  @Test
  void findUnassignedTrainers_Success() {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    List<Trainer> trainers = TrainerTestUtil.getUnassignedTrainersOnTrainee1();
    List<TrainerShortInfoDto> expectedResult = TrainerTestUtil.getUnassignedTrainerShortInfoDtosOnTrainee1();

    // When
    when(trainerRepository.findAllUnassignedByTraineeUsername(any())).thenReturn(trainers);
    when(trainerMapper.toTrainerShortInfoDto(any())).thenReturn(expectedResult.get(0),
        expectedResult.get(1), expectedResult.get(2));

    List<TrainerShortInfoDto> result = trainerService.findUnassignedTrainers(traineeUsername);

    // Then
    verify(trainerRepository, times(1)).findAllUnassignedByTraineeUsername(any());
    verify(trainerMapper, times(3)).toTrainerShortInfoDto(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainerShortInfoDto1(),
        TrainerTestUtil.getTrainerShortInfoDto3(),
        TrainerTestUtil.getTrainerShortInfoDto4()
    ));
  }
}
