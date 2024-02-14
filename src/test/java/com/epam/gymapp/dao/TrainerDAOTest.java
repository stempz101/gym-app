package com.epam.gymapp.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.utils.UserUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerDAOTest {

  @InjectMocks
  private TrainerDAO trainerDAO;

  @Mock
  private Map<Long, Trainer> trainerStorage;

  @Test
  void afterPropertiesSet_Success() {
    // Given
    Set<Long> keySet = Set.of(2L, 1L, 5L, 3L);

    // When
    when(trainerStorage.isEmpty()).thenReturn(false);
    when(trainerStorage.keySet()).thenReturn(keySet);

    trainerDAO.afterPropertiesSet();

    // Then
    verify(trainerStorage, times(1)).isEmpty();
    verify(trainerStorage, times(1)).keySet();
  }

  @Test
  void save_TrainerIsNull_Success() {
    // When
    Trainer result = trainerDAO.save(null);

    // Then
    assertNull(result);
  }

  @Test
  void save_TrainerWithoutId_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();
    trainer.setUserId(null);
    Trainer expectedResult = TrainerTestUtil.getTrainer1();

    // When
    when(trainerStorage.put(anyLong(), any(Trainer.class))).thenReturn(expectedResult);

    Trainer result = trainerDAO.save(trainer);

    // Then
    verify(trainerStorage, times(1)).put(anyLong(), any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void save_TrainerWithId_Success() {
    // Given
    Trainer oldTrainer = TrainerTestUtil.getTrainer1();
    Trainer trainer = TrainerTestUtil.getUpdatedTrainer1();

    // When
    when(trainerStorage.get(anyLong())).thenReturn(oldTrainer);

    Trainer result = trainerDAO.save(trainer);

    // Then
    verify(trainerStorage, times(1)).get(anyLong());

    assertThat(result, samePropertyValuesAs(trainer));
  }

  @Test
  void findAll_Success() {
    // Given
    List<Trainer> expectedResult = TrainerTestUtil.getTrainers();

    // When
    when(trainerStorage.values()).thenReturn(expectedResult);

    List<Trainer> result = trainerDAO.findAll();

    // Then
    verify(trainerStorage, times(1)).values();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainerTestUtil.getTrainer1(),
        TrainerTestUtil.getTrainer2(),
        TrainerTestUtil.getTrainer3()
    ));
  }

  @Test
  void findById_UserFound_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();
    Optional<Trainer> expectedResult = Optional.of(trainer);

    // When
    when(trainerStorage.get(anyLong())).thenReturn(trainer);

    Optional<Trainer> result = trainerDAO.findById(trainer.getUserId());

    // Then
    verify(trainerStorage, times(1)).get(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void findById_UserNotFound_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();
    Optional<Trainee> expectedResult = Optional.empty();

    // When
    when(trainerStorage.get(anyLong())).thenReturn(null);

    Optional<Trainer> result = trainerDAO.findById(trainer.getUserId());

    // Then
    verify(trainerStorage, times(1)).get(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void getLastCountedAppearances_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();
    List<Trainer> values = List.of(
        trainer,
        TrainerTestUtil.getTrainer2(),
        TrainerTestUtil.getTrainer3()
    );
    int expectedResult = 2;

    try(MockedStatic<UserUtils> userUtils = mockStatic(UserUtils.class)) {
      // When
      when(trainerStorage.values()).thenReturn(values);
      userUtils.when(() -> UserUtils.getAppearanceFromFoundUsername(any())).thenReturn(0, 2);

      int result = trainerDAO.getLastCountedAppearances(
          trainer.getFirstName(), trainer.getLastName());

      // Then
      verify(trainerStorage, times(1)).values();
      userUtils.verify(() -> UserUtils.getAppearanceFromFoundUsername(any()), times(2));

      assertEquals(expectedResult, result);
    }
  }

  @Test
  void existsById_IfExists_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();

    // When
    when(trainerStorage.get(anyLong())).thenReturn(trainer);

    boolean result = trainerDAO.existsById(trainer.getUserId());

    // Then
    verify(trainerStorage, times(1)).get(anyLong());

    assertTrue(result);
  }

  @Test
  void existsById_IfNotExists_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();

    // When
    when(trainerStorage.get(anyLong())).thenReturn(null);

    boolean result = trainerDAO.existsById(trainer.getUserId());

    // Then
    verify(trainerStorage, times(1)).get(anyLong());

    assertFalse(result);
  }

  @Test
  void existsByUsername_IfExists_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer2();
    List<Trainer> values = List.of(
        TrainerTestUtil.getTrainer3(),
        TrainerTestUtil.getTrainer1(),
        trainer
    );

    // When
    when(trainerStorage.values()).thenReturn(values);

    boolean result = trainerDAO.existsByUsername(trainer.getUsername());

    // Then
    verify(trainerStorage, times(1)).values();

    assertTrue(result);
  }

  @Test
  void existsByUsername_IfNotExists_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer2();
    trainer.setUsername("some_username");
    List<Trainer> values = List.of(
        TrainerTestUtil.getTrainer3(),
        TrainerTestUtil.getTrainer1(),
        TrainerTestUtil.getTrainer2()
    );

    // When
    when(trainerStorage.values()).thenReturn(values);

    boolean result = trainerDAO.existsByUsername(trainer.getUsername());

    // Then
    verify(trainerStorage, times(1)).values();

    assertFalse(result);
  }
}
