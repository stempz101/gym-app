package com.epam.gymapp.facade;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.service.TraineeService;
import com.epam.gymapp.service.TrainerService;
import com.epam.gymapp.service.TrainingService;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GymFacadeTest {

  @InjectMocks
  private GymFacade gymFacade;

  @Mock
  private TraineeService traineeService;

  @Mock
  private TrainerService trainerService;

  @Mock
  private TrainingService trainingService;

  @Test
  void createTraineeProfile_Success() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    Trainee expectedResult = TraineeTestUtil.getTrainee1();

    // When
    when(traineeService.create(any())).thenReturn(expectedResult);

    Trainee result = gymFacade.createTraineeProfile(traineeCreateDto);

    // Then
    verify(traineeService, times(1)).create(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void updateTraineeProfile_Success() {
    // Given
    Trainee expectedResult = TraineeTestUtil.getUpdatedTrainee1();

    // When
    when(traineeService.update(any())).thenReturn(expectedResult);

    Trainee result = gymFacade.updateTraineeProfile(expectedResult);

    // Then
    verify(traineeService, times(1)).update(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectAllTrainees_Success() {
    // Given
    List<Trainee> expectedResult = TraineeTestUtil.getTrainees();

    // When
    when(traineeService.selectTrainees()).thenReturn(expectedResult);

    List<Trainee> result = gymFacade.selectAllTrainees();

    // Then
    verify(traineeService, times(1)).selectTrainees();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TraineeTestUtil.getTrainee1(),
        TraineeTestUtil.getTrainee2(),
        TraineeTestUtil.getTrainee3()
    ));
  }

  @Test
  void selectTraineeProfile_Success() {
    // Given
    Trainee expectedResult = TraineeTestUtil.getTrainee1();

    // When
    when(traineeService.selectTrainee(anyLong())).thenReturn(expectedResult);

    Trainee result = gymFacade.selectTraineeProfile(expectedResult.getUserId());

    // Then
    verify(traineeService, times(1)).selectTrainee(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void deleteTraineeProfile_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    doNothing().when(traineeService).delete(anyLong());

    gymFacade.deleteTraineeProfile(trainee.getUserId());

    // Then
    verify(traineeService, times(1)).delete(anyLong());
  }

  @Test
  void createTrainerProfile_Success() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    Trainer expectedResult = TrainerTestUtil.getTrainer1();

    // When
    when(trainerService.create(any())).thenReturn(expectedResult);

    Trainer result = gymFacade.createTrainerProfile(trainerCreateDto);

    // Then
    verify(trainerService, times(1)).create(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }
// TODO: asdadsfasdf
  @Test
  void updateTrainerProfile_Success() {
    // Given
    Trainer expectedResult = TrainerTestUtil.getUpdatedTrainer1();

    // When
    when(trainerService.update(any())).thenReturn(expectedResult);

    Trainer result = gymFacade.updateTrainerProfile(expectedResult);

    // Then
    verify(trainerService, times(1)).update(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectAllTrainers_Success() {
    // Given
    List<Trainer> expectedResult = TrainerTestUtil.getTrainers();

    // When
    when(trainerService.selectTrainers()).thenReturn(expectedResult);

    List<Trainer> result = gymFacade.selectAllTrainers();

    // Then
    verify(trainerService, times(1)).selectTrainers();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainer1(),
        TrainerTestUtil.getTrainer2(),
        TrainerTestUtil.getTrainer3()
    ));
  }

  @Test
  void selectTrainerProfile_Success() {
    // Given
    Trainer expectedResult = TrainerTestUtil.getTrainer1();

    // When
    when(trainerService.selectTrainer(anyLong())).thenReturn(expectedResult);

    Trainer result = gymFacade.selectTrainerProfile(expectedResult.getUserId());

    // Then
    verify(trainerService, times(1)).selectTrainer(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void createTraining_Success() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    Training expectedResult = TrainingTestUtil.getTraining1();

    // When
    when(trainingService.create(any())).thenReturn(expectedResult);

    Training result = gymFacade.createTraining(trainingCreateDto);

    // Then
    verify(trainingService, times(1)).create(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectAllTrainings_Success() {
    // Given
    List<Training> expectedResult = TrainingTestUtil.getTrainings();

    // When
    when(trainingService.selectTrainings()).thenReturn(expectedResult);

    List<Training> result = gymFacade.selectAllTrainings();

    // Then
    verify(trainingService, times(1)).selectTrainings();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTraining1(),
        TrainingTestUtil.getTraining2(),
        TrainingTestUtil.getTraining3()
    ));
  }

  @Test
  void selectTrainingProfile_Success() {
    // Given
    Training expectedResult = TrainingTestUtil.getTraining1();

    // When
    when(trainingService.selectTraining(anyLong())).thenReturn(expectedResult);

    Training result = gymFacade.selectTraining(expectedResult.getId());

    // Then
    verify(trainingService, times(1)).selectTraining(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }
}
