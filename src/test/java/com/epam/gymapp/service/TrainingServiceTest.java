package com.epam.gymapp.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.dao.TraineeDAO;
import com.epam.gymapp.dao.TrainerDAO;
import com.epam.gymapp.dao.TrainingDAO;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.TrainingNotFoundException;
import com.epam.gymapp.exception.TrainingValidationException;
import com.epam.gymapp.mapper.TrainingMapper;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.validator.TrainingCreateDtoValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
  private TrainingDAO trainingDAO;

  @Mock
  private TraineeDAO traineeDAO;

  @Mock
  private TrainerDAO trainerDAO;

  @Spy
  private TrainingCreateDtoValidator trainingCreateDtoValidator;

  @Mock
  private TrainingMapper trainingMapper;

  @BeforeEach
  void setUp() {
    trainingService.setTrainingCreateDtoValidator(trainingCreateDtoValidator);
    trainingService.setTrainingMapper(trainingMapper);
  }

  @Test
  void create_Success() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    Training mappedTraining = TrainingTestUtil.getTraining1FromTrainingCreateDto();
    Training expectedResult = TrainingTestUtil.getTraining1();

    // When
    when(traineeDAO.existsById(anyLong())).thenReturn(true);
    when(trainerDAO.existsById(anyLong())).thenReturn(true);
    when(trainingMapper.toTraining(any())).thenReturn(mappedTraining);
    when(trainingDAO.save(any())).thenReturn(expectedResult);

    Training result = trainingService.create(trainingCreateDto);

    // Then
    verify(trainingCreateDtoValidator, times(1)).validate(any());
    verify(traineeDAO, times(1)).existsById(anyLong());
    verify(trainerDAO, times(1)).existsById(anyLong());
    verify(trainingMapper, times(1)).toTraining(any());
    verify(trainingDAO, times(1)).save(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void create_TrainingCreateDtoIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> trainingService.create(null));
  }

  @Test
  void create_TraineeIdIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTraineeId(null);

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_TrainerIdIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTrainerId(null);

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_NameIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName(null);

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_NameIsEmpty_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName("");

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_NameIsBlank_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName("    ");

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_TypeIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setType(null);

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_TypeNameIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setType(new TrainingType(null));

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_TypeNameIsEmpty_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setType(new TrainingType(""));

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_TypeNameIsBlank_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setType(new TrainingType("    "));

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_DateIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setDate(null);

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_DurationIsNull_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setDuration(null);

    // When & Then
    assertThrows(TrainingValidationException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_TraineeNotFound_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();

    // When
    when(traineeDAO.existsById(anyLong())).thenReturn(false);

    // Then
    assertThrows(TraineeNotFoundException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void create_TrainerNotFound_Failure() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();

    // When
    when(traineeDAO.existsById(anyLong())).thenReturn(true);
    when(trainerDAO.existsById(anyLong())).thenReturn(false);

    // Then
    assertThrows(TrainerNotFoundException.class, () -> trainingService.create(trainingCreateDto));
  }

  @Test
  void selectTrainings_Success() {
    // Given
    List<Training> expectedResult = TrainingTestUtil.getTrainings();

    // When
    when(trainingDAO.findAll()).thenReturn(expectedResult);

    List<Training> result = trainingService.selectTrainings();

    // Then
    verify(trainingDAO, times(1)).findAll();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTraining1(),
        TrainingTestUtil.getTraining2(),
        TrainingTestUtil.getTraining3()
    ));
  }

  @Test
  void selectTraining_Success() {
    // Given
    Training expectedResult = TrainingTestUtil.getTraining1();

    // When
    when(trainingDAO.findById(anyLong())).thenReturn(Optional.of(expectedResult));

    Training result = trainingService.selectTraining(expectedResult.getId());

    // Then
    verify(trainingDAO, times(1)).findById(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectTraining_TrainingNotFound_Failure() {
    // Given
    Training training = TrainingTestUtil.getTraining1();

    // When
    when(trainingDAO.findById(anyLong())).thenReturn(Optional.empty());

    // Then
    assertThrows(TrainingNotFoundException.class, () -> trainingService.selectTraining(training.getId()));
  }
}
