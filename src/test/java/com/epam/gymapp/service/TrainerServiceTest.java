package com.epam.gymapp.service;

import static com.epam.gymapp.test.utils.TrainerTestUtil.TEST_TRAINER_PASSWORD_1;
import static com.epam.gymapp.test.utils.TrainerTestUtil.TEST_TRAINER_USERNAME_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.dao.TrainerDAO;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.exception.TrainerExistsWithUsernameException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.TrainerValidationException;
import com.epam.gymapp.mapper.TrainerMapper;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.utils.UserUtils;
import com.epam.gymapp.validator.TrainerCreateDtoValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
  private TrainerDAO trainerDAO;

  @Spy
  private TrainerCreateDtoValidator trainerCreateDtoValidator;

  @Mock
  private TrainerMapper trainerMapper;

  @BeforeEach
  void setUp() {
    trainerService.setTrainerCreateDtoValidator(trainerCreateDtoValidator);
    trainerService.setTrainerMapper(trainerMapper);
  }

  @Test
  void create_Success() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    Trainer mappedTrainer = TrainerTestUtil.getTrainer1FromTrainerCreateDto();
    Trainer expectedResult = TrainerTestUtil.getTrainer1();

    try(MockedStatic<UserUtils> userUtils = mockStatic(UserUtils.class)) {
      // When
      when(trainerMapper.toTrainer(any())).thenReturn(mappedTrainer);
      when(trainerDAO.getLastCountedAppearances(any(), any())).thenReturn(0);
      userUtils.when(() -> UserUtils.buildUsername(any(), anyLong())).thenReturn(
          TEST_TRAINER_USERNAME_1);
      userUtils.when(UserUtils::generatePassword).thenReturn(TEST_TRAINER_PASSWORD_1);
      when(trainerDAO.save(any())).thenReturn(expectedResult);

      Trainer result = trainerService.create(trainerCreateDto);

      // Then
      verify(trainerCreateDtoValidator, times(1)).validate(any());
      verify(trainerMapper, times(1)).toTrainer(any());
      verify(trainerDAO, times(1)).getLastCountedAppearances(any(), any());
      userUtils.verify(() -> UserUtils.buildUsername(any(), anyLong()), times(1));
      userUtils.verify(UserUtils::generatePassword, times(1));
      verify(trainerDAO, times(1)).save(any());

      assertThat(result, samePropertyValuesAs(expectedResult));
    }
  }

  @Test
  void create_TrainerCreateDtoIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> trainerService.create(null));
  }

  @Test
  void create_FirstNameIsNull_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName(null);

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_FirstNameIsEmpty_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName("");

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_FirstNameIsBlank_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName("   ");

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_LastNameIsNull_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName(null);

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_LastNameIsEmpty_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName("");

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_LastNameIsBlank_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName("   ");

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_SpecializationIsNull_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization(null);

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_SpecializationNameIsNull_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization(new TrainingType(null));

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_SpecializationNameIsEmpty_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization(new TrainingType(""));

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void create_SpecializationNameIsBlank_Failure() {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization(new TrainingType("   "));

    // When & Then
    assertThrows(TrainerValidationException.class, () -> trainerService.create(trainerCreateDto));
  }

  @Test
  void selectTrainers_Success() {
    // Given
    List<Trainer> expectedResult = TrainerTestUtil.getTrainers();

    // When
    when(trainerDAO.findAll()).thenReturn(expectedResult);

    List<Trainer> result = trainerService.selectTrainers();

    // Then
    verify(trainerDAO, times(1)).findAll();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainer1(),
        TrainerTestUtil.getTrainer2(),
        TrainerTestUtil.getTrainer3()
    ));
  }

  @Test
  void selectTrainer_Success() {
    // Given
    Trainer expectedResult = TrainerTestUtil.getTrainer1();

    // When
    when(trainerDAO.findById(anyLong())).thenReturn(Optional.of(expectedResult));

    Trainer result = trainerService.selectTrainer(expectedResult.getUserId());

    // Then
    verify(trainerDAO, times(1)).findById(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void selectTrainer_TrainerNotFound_Failure() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();

    // When
    when(trainerDAO.findById(anyLong())).thenReturn(Optional.empty());

    // Then
    assertThrows(TrainerNotFoundException.class, () -> trainerService.selectTrainer(trainer.getUserId()));
  }

  @Test
  void update_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();

    // When
    when(trainerDAO.existsById(anyLong())).thenReturn(true);
    when(trainerDAO.existsByUsername(any())).thenReturn(false);
    when(trainerDAO.save(any())).thenReturn(trainer);

    Trainer result = trainerService.update(trainer);

    // Then
    verify(trainerDAO, times(1)).existsById(anyLong());
    verify(trainerDAO, times(1)).existsByUsername(any());
    verify(trainerDAO, times(1)).save(any());

    assertThat(result, samePropertyValuesAs(trainer));
  }

  @Test
  void update_TrainerIsNull_Failure() {
    // When & Then
    assertThrows(IllegalArgumentException.class, () -> trainerService.update(null));
  }

  @Test
  void update_TrainerNotFoundIfUserIdIsNull_Failure() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();
    trainer.setUserId(null);

    // When
    when(trainerDAO.existsById(null)).thenReturn(false);

    // Then
    assertThrows(TrainerNotFoundException.class, () -> trainerService.update(trainer));
  }

  @Test
  void update_TrainerNotFoundIfUserIdIsNotNull_Failure() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();

    // When
    when(trainerDAO.existsById(anyLong())).thenReturn(false);

    // Then
    assertThrows(TrainerNotFoundException.class, () -> trainerService.update(trainer));
  }

  @Test
  void update_TrainerWithUsernameExists_Failure() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();

    // When
    when(trainerDAO.existsById(anyLong())).thenReturn(true);
    when(trainerDAO.existsByUsername(any())).thenReturn(true);

    // Then
    assertThrows(TrainerExistsWithUsernameException.class, () -> trainerService.update(trainer));
  }
}
