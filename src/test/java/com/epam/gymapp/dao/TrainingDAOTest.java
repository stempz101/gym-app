package com.epam.gymapp.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.model.Training;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingDAOTest {

  @InjectMocks
  private TrainingDAO trainingDAO;

  @Mock
  private Map<Long, Training> trainingStorage;

  @Test
  void afterPropertiesSet_Success() {
    // Given
    Set<Long> keySet = Set.of(5L, 2L, 1L, 3L);

    // When
    when(trainingStorage.isEmpty()).thenReturn(false);
    when(trainingStorage.keySet()).thenReturn(keySet);

    trainingDAO.afterPropertiesSet();

    // Then
    verify(trainingStorage, times(1)).isEmpty();
    verify(trainingStorage, times(1)).keySet();
  }

  @Test
  void save_TrainingIsNull_Success() {
    // When
    Training result = trainingDAO.save(null);

    // Then
    assertNull(result);
  }

  @Test
  void save_TrainingIsNotNull_Success() {
    // Given
    Training training = TrainingTestUtil.getTraining1();
    training.setId(null);
    Training expectedResult = TrainingTestUtil.getTraining1();

    // When
    when(trainingStorage.put(anyLong(), any(Training.class))).thenReturn(expectedResult);

    Training result = trainingDAO.save(training);

    // Then
    verify(trainingStorage, times(1)).put(anyLong(), any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void findAll_Success() {
    // Given
    List<Training> expectedResult = TrainingTestUtil.getTrainings();

    // When
    when(trainingStorage.values()).thenReturn(expectedResult);

    List<Training> result = trainingDAO.findAll();

    // Then
    verify(trainingStorage, times(1)).values();

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(
        TrainingTestUtil.getTraining1(),
        TrainingTestUtil.getTraining2(),
        TrainingTestUtil.getTraining3()
    ));
  }

  @Test
  void findById_UserFound_Success() {
    // Given
    Training training = TrainingTestUtil.getTraining1();
    Optional<Training> expectedResult = Optional.of(training);

    // When
    when(trainingStorage.get(anyLong())).thenReturn(training);

    Optional<Training> result = trainingDAO.findById(training.getId());

    // Then
    verify(trainingStorage, times(1)).get(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void findById_UserNotFound_Success() {
    // Given
    Training training = TrainingTestUtil.getTraining1();
    Optional<Training> expectedResult = Optional.empty();

    // When
    when(trainingStorage.get(anyLong())).thenReturn(null);

    Optional<Training> result = trainingDAO.findById(training.getId());

    // Then
    verify(trainingStorage, times(1)).get(anyLong());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }
}
