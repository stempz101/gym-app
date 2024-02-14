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
import com.epam.gymapp.test.utils.TraineeTestUtil;
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
public class TraineeDAOTest {

  @InjectMocks
  private TraineeDAO traineeDAO;

  @Mock
  private Map<Long, Trainee> traineeStorage;

  @Test
  void afterPropertiesSet_Success() {
    // Given
    Set<Long> keySet = Set.of(1L, 2L, 3L, 5L);

    // When
    when(traineeStorage.isEmpty()).thenReturn(false);
    when(traineeStorage.keySet()).thenReturn(keySet);

    traineeDAO.afterPropertiesSet();

    // Then
    verify(traineeStorage, times(1)).isEmpty();
    verify(traineeStorage, times(1)).keySet();
  }

  @Test
  void save_TraineeIsNull_Success() {
    // When
    Trainee result = traineeDAO.save(null);

    // Then
    assertNull(result);
  }

  @Test
  void save_TraineeWithoutId_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();
    trainee.setUserId(null);
    Trainee expectedResult = TraineeTestUtil.getTrainee1();

    // When
    when(traineeStorage.put(anyLong(), any(Trainee.class))).thenReturn(expectedResult);

    Trainee result = traineeDAO.save(trainee);

    // Then
    verify(traineeStorage, times(1)).put(anyLong(), any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void save_TraineeWithId_Success() {
    // Given
    Trainee oldTrainee = TraineeTestUtil.getTrainee1();
    Trainee trainee = TraineeTestUtil.getUpdatedTrainee1();

    // When
    when(traineeStorage.get(anyLong())).thenReturn(oldTrainee);

    Trainee result = traineeDAO.save(trainee);

    // Then
    verify(traineeStorage, times(1)).get(anyLong());

    assertThat(result, samePropertyValuesAs(trainee));
  }

  @Test
  void findAll_Success() {
    // Given
    List<Trainee> expectedResult = TraineeTestUtil.getTrainees();

    // When
    when(traineeStorage.values()).thenReturn(expectedResult);

    List<Trainee> result = traineeDAO.findAll();

    // Then
    verify(traineeStorage, times(1)).values();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TraineeTestUtil.getTrainee1(),
        TraineeTestUtil.getTrainee2(),
        TraineeTestUtil.getTrainee3()
    ));
  }

  @Test
  void findById_UserFound_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();
    Optional<Trainee> expectedResult = Optional.of(trainee);

    // When
    when(traineeStorage.get(anyLong())).thenReturn(trainee);

    Optional<Trainee> result = traineeDAO.findById(trainee.getUserId());

    // Then
    verify(traineeStorage, times(1)).get(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void findById_UserNotFound_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();
    Optional<Trainee> expectedResult = Optional.empty();

    // When
    when(traineeStorage.get(anyLong())).thenReturn(null);

    Optional<Trainee> result = traineeDAO.findById(trainee.getUserId());

    // Then
    verify(traineeStorage, times(1)).get(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void deleteById_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeStorage.remove(anyLong())).thenReturn(trainee);

    traineeDAO.deleteById(trainee.getUserId());

    // Then
    verify(traineeStorage, times(1)).remove(anyLong());
  }

  @Test
  void getLastCountedAppearances_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();
    List<Trainee> values = List.of(
        trainee,
        TraineeTestUtil.getTrainee2(),
        TraineeTestUtil.getTrainee3()
    );
    int expectedResult = 3;

    try (MockedStatic<UserUtils> userUtils = mockStatic(UserUtils.class)) {
      // When
      when(traineeStorage.values()).thenReturn(values);
      userUtils.when(() -> UserUtils.getAppearanceFromFoundUsername(any())).thenReturn(0, 3);

      int result = traineeDAO.getLastCountedAppearances(
          trainee.getFirstName(), trainee.getLastName());

      // Then
      verify(traineeStorage, times(1)).values();
      userUtils.verify(() -> UserUtils.getAppearanceFromFoundUsername(any()), times(2));

      assertEquals(expectedResult, result);
    }
  }

  @Test
  void existsById_IfExists_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeStorage.get(anyLong())).thenReturn(trainee);

    boolean result = traineeDAO.existsById(trainee.getUserId());

    // Then
    verify(traineeStorage, times(1)).get(anyLong());

    assertTrue(result);
  }

  @Test
  void existsById_IfNotExists_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee1();

    // When
    when(traineeStorage.get(anyLong())).thenReturn(null);

    boolean result = traineeDAO.existsById(trainee.getUserId());

    // Then
    verify(traineeStorage, times(1)).get(anyLong());

    assertFalse(result);
  }

  @Test
  void existsByUsername_IfExists_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee2();
    List<Trainee> values = List.of(
        TraineeTestUtil.getTrainee3(),
        TraineeTestUtil.getTrainee1(),
        trainee
    );

    // When
    when(traineeStorage.values()).thenReturn(values);

    boolean result = traineeDAO.existsByUsername(trainee.getUsername());

    // Then
    verify(traineeStorage, times(1)).values();

    assertTrue(result);
  }

  @Test
  void existsByUsername_IfNotExists_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee2();
    trainee.setUsername("some_username");
    List<Trainee> values = List.of(
        TraineeTestUtil.getTrainee3(),
        TraineeTestUtil.getTrainee1(),
        TraineeTestUtil.getTrainee2()
    );

    // When
    when(traineeStorage.values()).thenReturn(values);

    boolean result = traineeDAO.existsByUsername(trainee.getUsername());

    // Then
    verify(traineeStorage, times(1)).values();

    assertFalse(result);
  }
}
