package com.epam.gymapp.facade;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.service.TraineeService;
import com.epam.gymapp.service.TrainerService;
import com.epam.gymapp.service.TrainingService;
import com.epam.gymapp.service.UserService;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import java.time.LocalDate;
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

  @Mock
  private UserService userService;

  @Test
  void createTraineeProfile_Success() {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    UserCredentialsDto expectedResult = UserTestUtil.getTraineeUserCredentialsDto1();

    // When
    when(traineeService.createTrainee(any())).thenReturn(expectedResult);

    UserCredentialsDto result = gymFacade.createTraineeProfile(traineeCreateDto);

    // Then
    verify(traineeService, times(1)).createTrainee(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void createTrainerProfile_Success() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    UserCredentialsDto expectedResult = UserTestUtil.getTrainerUserCredentialsDto1();

    // When
    when(trainerService.createTrainer(any())).thenReturn(expectedResult);

    UserCredentialsDto result = gymFacade.createTrainerProfile(trainerCreateDto);

    // Then
    verify(trainerService, times(1)).createTrainer(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectAllTrainees_Success() {
    // Given
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    List<TraineeInfoDto> expectedResult = TraineeTestUtil.getTraineeInfoDtos();

    // When
    doNothing().when(userService).authenticate(any());
    when(traineeService.selectTrainees()).thenReturn(expectedResult);

    List<TraineeInfoDto> result = gymFacade.selectAllTrainees(userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(traineeService, times(1)).selectTrainees();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TraineeTestUtil.getTraineeInfoDto1(),
        TraineeTestUtil.getTraineeInfoDto2(),
        TraineeTestUtil.getTraineeInfoDto3()
    ));
  }

  @Test
  void selectAllTrainers_Success() {
    // Given
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    List<TrainerInfoDto> expectedResult = TrainerTestUtil.getTrainerInfoDtos();

    // When
    doNothing().when(userService).authenticate(any());
    when(trainerService.selectTrainers()).thenReturn(expectedResult);

    List<TrainerInfoDto> result = gymFacade.selectAllTrainers(userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(trainerService, times(1)).selectTrainers();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainerInfoDto1(),
        TrainerTestUtil.getTrainerInfoDto2(),
        TrainerTestUtil.getTrainerInfoDto3()
    ));
  }

  @Test
  void selectTraineeProfile_Success() {
    // Given
    UserCredentialsDto userCreds = UserTestUtil.getTraineeUserCredentialsDto1();
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1();

    // When
    doNothing().when(userService).authenticate(any());
    when(traineeService.selectTrainee(any())).thenReturn(expectedResult);

    TraineeInfoDto result = gymFacade.selectTraineeProfile(userCreds.getUsername(), userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(traineeService, times(1)).selectTrainee(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectTrainerProfile_Success() {
    // Given
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    TrainerInfoDto expectedResult = TrainerTestUtil.getTrainerInfoDto1();

    // When
    doNothing().when(userService).authenticate(any());
    when(trainerService.selectTrainer(any())).thenReturn(expectedResult);

    TrainerInfoDto result = gymFacade.selectTrainerProfile(userCreds.getUsername(), userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(trainerService, times(1)).selectTrainer(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void changePassword_Success() {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();

    // When
    doNothing().when(userService).changePassword(any());

    gymFacade.changePassword(changePasswordDto);

    // Then
    verify(userService, times(1)).changePassword(any());
  }

  @Test
  void updateTraineeProfile_Success() {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1();

    // When
    doNothing().when(userService).authenticate(any());
    when(traineeService.updateTrainee(any())).thenReturn(expectedResult);

    TraineeInfoDto result = gymFacade.updateTraineeProfile(traineeUpdateDto, userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(traineeService, times(1)).updateTrainee(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void updateTrainerProfile_Success() {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    TrainerInfoDto expectedResult = TrainerTestUtil.getTrainerInfoDto1();

    // When
    doNothing().when(userService).authenticate(any());
    when(trainerService.updateTrainer(any())).thenReturn(expectedResult);

    TrainerInfoDto result = gymFacade.updateTrainerProfile(trainerUpdateDto, userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(trainerService, times(1)).updateTrainer(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void changeActivationStatus_Success() {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();

    // Given
    doNothing().when(userService).authenticate(any());
    doNothing().when(userService).changeActivationStatus(any());

    gymFacade.changeActivationStatus(userActivation, userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(userService, times(1)).changeActivationStatus(any());
  }

  @Test
  void deleteTraineeProfile_Success() {
    // Given
    String username = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();

    // When
    doNothing().when(userService).authenticate(any());
    doNothing().when(traineeService).deleteTrainee(any());

    gymFacade.deleteTraineeProfile(username, userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(traineeService, times(1)).deleteTrainee(any());
  }

  @Test
  void getTraineeTrainings_Success() {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    LocalDate fromDate = LocalDate.of(2024, 2, 5);
    LocalDate toDate = LocalDate.of(2024, 2, 20);
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    List<TrainingInfoDto> expectedResult = TrainingTestUtil.getTrainingInfoDtosOfTrainee2();

    // When
    doNothing().when(userService).authenticate(any());
    when(traineeService.findTraineeTrainings(any(), any(), any(), any(), any()))
        .thenReturn(expectedResult);

    List<TrainingInfoDto> result = gymFacade.getTraineeTrainings(traineeUsername,
        fromDate, toDate, null, null, userCreds);

    verify(userService, times(1)).authenticate(any());
    verify(traineeService, times(1)).findTraineeTrainings(any(), any(), any(), any(), any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTrainingInfoDto2(),
        TrainingTestUtil.getTrainingInfoDto5()
    ));
  }

  @Test
  void getTrainerTrainings_Success() {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    LocalDate fromDate = LocalDate.of(2024, 2, 5);
    LocalDate toDate = LocalDate.of(2024, 2, 20);
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    List<TrainingInfoDto> expectedResult = TrainingTestUtil.getTrainingInfoDtosOfTrainer2();

    // When
    doNothing().when(userService).authenticate(any());
    when(trainerService.findTrainerTrainings(any(), any(), any(), any())).thenReturn(expectedResult);

    List<TrainingInfoDto> result = gymFacade.getTrainerTrainings(trainerUsername,
        fromDate, toDate, null, userCreds);

    verify(userService, times(1)).authenticate(any());
    verify(trainerService, times(1)).findTrainerTrainings(any(), any(), any(), any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTrainingInfoDto1(),
        TrainingTestUtil.getTrainingInfoDto6()
    ));
  }

  @Test
  void addTraining_Success() {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    TrainingInfoDto expectedResult = TrainingTestUtil.getTrainingInfoDto1();

    // When
    doNothing().when(userService).authenticate(any());
    when(trainingService.createTraining(any())).thenReturn(expectedResult);

    TrainingInfoDto result = gymFacade.addTraining(trainingCreateDto, userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(trainingService, times(1)).createTraining(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectAllTrainings_Success() {
    // Given
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    List<TrainingInfoDto> expectedResult = TrainingTestUtil.getTrainingInfoDtos();

    // When
    doNothing().when(userService).authenticate(any());
    when(trainingService.selectTrainings()).thenReturn(expectedResult);

    List<TrainingInfoDto> result = gymFacade.selectAllTrainings(userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(trainingService, times(1)).selectTrainings();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTrainingInfoDto1(),
        TrainingTestUtil.getTrainingInfoDto2()
    ));
  }

  @Test
  void getUnassignedTrainersOnTrainee_Success() {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    List<TrainerShortInfoDto> expectedResult = TrainerTestUtil.getUnassignedTrainerShortInfoDtosOnTrainee1();

    // When
    doNothing().when(userService).authenticate(any());
    when(trainerService.findUnassignedTrainers(any())).thenReturn(expectedResult);

    List<TrainerShortInfoDto> result = gymFacade
        .getUnassignedTrainersOnTrainee(traineeUsername, userCreds);

    verify(userService, times(1)).authenticate(any());
    verify(trainerService, times(1)).findUnassignedTrainers(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainerShortInfoDto1(),
        TrainerTestUtil.getTrainerShortInfoDto3(),
        TrainerTestUtil.getTrainerShortInfoDto4()
    ));
  }

  @Test
  void updateTrainerListOfTrainee_Success() {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    UserCredentialsDto userCreds = UserTestUtil.getTrainerUserCredentialsDto1();
    List<TrainerShortInfoDto> expectedResult = TraineeTestUtil.getTraineeUpdatedTrainerShortInfoDtosList2();

    // When
    doNothing().when(userService).authenticate(any());
    when(traineeService.updateTrainerList(any())).thenReturn(expectedResult);

    List<TrainerShortInfoDto> result = gymFacade.updateTrainerListOfTrainee(
        traineeTrainersUpdateDto, userCreds);

    // Then
    verify(userService, times(1)).authenticate(any());
    verify(traineeService, times(1)).updateTrainerList(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainerShortInfoDto1(),
        TrainerTestUtil.getTrainerShortInfoDto2()
    ));
  }
}
