package com.epam.gymapp.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.TrainingValidationException;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.repository.TraineeRepository;
import com.epam.gymapp.repository.TrainerRepository;
import com.epam.gymapp.repository.TrainingRepository;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.validator.TrainingValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

  @InjectMocks
  private TrainingService trainingService;

  @Mock
  private TrainingRepository trainingRepository;

  @Mock
  private TraineeRepository traineeRepository;

  @Mock
  private TrainerRepository trainerRepository;

  @Spy
  private TrainingValidator trainingValidator;

  @Mock
  private TrainingMapper trainingMapper;


  @Test
  void createTraining_Success() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    Training mappedTraining = TrainingTestUtil.getTraining1FromTrainingCreateDto();
    Training createdTraining = TrainingTestUtil.getTraining1();
    Trainee trainee = TraineeTestUtil.getTrainee1();
    Trainer trainer = TrainerTestUtil.getTrainer2();
    TrainingInfoDto expectedResult = TrainingTestUtil.getTrainingInfoDto1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    when(trainerRepository.findByUsername(any())).thenReturn(Optional.of(trainer));
    when(trainingMapper.toTraining(any())).thenReturn(mappedTraining);
    when(trainingRepository.save(any())).thenReturn(createdTraining);
    when(trainingMapper.toTrainingInfoDto(any())).thenReturn(expectedResult);

    TrainingInfoDto result = trainingService.createTraining(trainingCreateDto);

    // Then
    verify(trainingValidator, times(1)).validate(any());
    verify(traineeRepository, times(1)).findByUsername(any());
    verify(trainerRepository, times(1)).findByUsername(any());
    verify(trainingMapper, times(1)).toTraining(any());
    verify(trainingRepository, times(1)).save(any());
    verify(trainingMapper, times(1)).toTrainingInfoDto(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void createTraining_TrainingCreateDtoIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> trainingService.createTraining(null));
  }

  @Test
  void createTraining_TraineeUsernameIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTraineeUsername(null);

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_TraineeUsernameIsEmpty_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTraineeUsername("");

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_TraineeUsernameIsBlank_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTraineeUsername("    ");

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_TrainerUsernameIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTrainerUsername(null);

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_TrainerUsernameIsEmpty_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTrainerUsername("");

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_TrainerUsernameIsBlank_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTrainerUsername("    ");

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_NameIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName(null);

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_NameIsEmpty_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName("");

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_NameIsBlank_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName("    ");

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_DateIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setDate(null);

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_DurationIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setDuration(null);

    // When & Then
    assertThrows(TrainingValidationException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_TraineeNotFound_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(TraineeNotFoundException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void createTraining_TrainerNotFound_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
    when(trainerRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(TrainerNotFoundException.class,
        () -> trainingService.createTraining(trainingCreateDto));
  }

  @Test
  void selectTrainings_Success() {
    // Given
    List<Training> trainings = TrainingTestUtil.getTrainings();
    List<TrainingInfoDto> expectedResult = TrainingTestUtil.getTrainingInfoDtos();

    // When
    when(trainingRepository.findAll()).thenReturn(trainings);
    when(trainingMapper.toTrainingInfoDto(any())).thenReturn(expectedResult.get(0),
        expectedResult.get(1));

    List<TrainingInfoDto> result = trainingService.selectTrainings();

    // Then
    verify(trainingRepository, times(1)).findAll();
    verify(trainingMapper, times(2)).toTrainingInfoDto(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTrainingInfoDto1(),
        TrainingTestUtil.getTrainingInfoDto2()
    ));
  }
}
