package com.epam.reportsmicroservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.reportsmicroservice.mapper.TrainerWorkloadMapper;
import com.epam.reportsmicroservice.model.TrainerWorkload;
import com.epam.reportsmicroservice.repository.TrainerWorkloadRepository;
import com.epam.reportsmicroservice.test.utils.TrainerWorkloadTestUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadServiceTest {

  @InjectMocks
  private TrainerWorkloadService trainerWorkloadService;

  @Mock
  private TrainerWorkloadRepository trainerWorkloadRepository;

  @Mock
  private TrainerWorkloadMapper trainerWorkloadMapper;

  @Test
  void retrieveTrainersWorkloadForMonth_Success() {
    // Given
    int year = 2024;
    int month = 4;
    String username = TrainerWorkloadTestUtil.TEST_TRAINER_USERNAME_2;
    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload2(2024, 4, 120);
    List<TrainerWorkload> fetchedTrainerWorkload = Collections.singletonList(trainerWorkload);
    TrainerWorkloadDto trainerWorkloadDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadDto2(2024, 4, 120);
    List<TrainerWorkloadDto> expectedResult = Collections.singletonList(trainerWorkloadDto);

    // When
    when(trainerWorkloadRepository.findAllByYearAndMonthAndUsername(anyInt(), any(), any()))
        .thenReturn(fetchedTrainerWorkload);
    when(trainerWorkloadMapper.toTrainerWorkloadDto(any())).thenReturn(trainerWorkloadDto);

    List<TrainerWorkloadDto> result = trainerWorkloadService
        .retrieveTrainersWorkloadForMonth(year, month, username);

    // Then
    verify(trainerWorkloadRepository, times(1))
        .findAllByYearAndMonthAndUsername(anyInt(), any(), any());
    verify(trainerWorkloadMapper, times(1)).toTrainerWorkloadDto(any());

    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(trainerWorkloadDto));
  }

  @Test
  void updateTrainersRecords_AddDurationAndSaveNewRecord_Success() {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto(2024, 3, 120, ActionType.ADD);
    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(2024, 3, 120);

    // When
    when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(any(), anyInt(), any()))
        .thenReturn(Optional.empty());
    when(trainerWorkloadMapper.toTrainerWorkload(any())).thenReturn(trainerWorkload);
    when(trainerWorkloadRepository.save(any())).thenReturn(trainerWorkload);

    trainerWorkloadService.updateTrainersWorkload(Collections.singletonList(trainerWorkloadUpdateDto));

    // Then
    verify(trainerWorkloadRepository, times(1))
        .findByUsernameAndYearAndMonth(any(), anyInt(), any());
    verify(trainerWorkloadMapper, times(1)).toTrainerWorkload(any());
    verify(trainerWorkloadRepository, times(1)).save(any());
  }

  @Test
  void updateTrainersRecords_AddDurationAndUpdateExistingRecord_Success() {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto(2024, 3, 120, ActionType.ADD);
    TrainerWorkload persistedTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(2024, 3, 20);
    TrainerWorkload updatedTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(2024, 3, 140);

    // When
    when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(any(), anyInt(), any()))
        .thenReturn(Optional.of(persistedTrainerWorkload));
    when(trainerWorkloadRepository.save(any())).thenReturn(updatedTrainerWorkload);

    trainerWorkloadService.updateTrainersWorkload(Collections.singletonList(trainerWorkloadUpdateDto));

    // Then
    verify(trainerWorkloadRepository, times(1))
        .findByUsernameAndYearAndMonth(any(), anyInt(), any());
    verify(trainerWorkloadRepository, times(1)).save(any());
  }

  @Test
  void updateTrainersRecords_SubtractDurationAndUpdateExistingRecord_Success() {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto(2024, 3, 120, ActionType.DELETE);
    TrainerWorkload persistedTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(2024, 3, 140);
    TrainerWorkload updatedTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(2024, 3, 20);

    // When
    when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(any(), anyInt(), any()))
        .thenReturn(Optional.of(persistedTrainerWorkload));
    when(trainerWorkloadRepository.save(any())).thenReturn(updatedTrainerWorkload);

    trainerWorkloadService.updateTrainersWorkload(Collections.singletonList(trainerWorkloadUpdateDto));

    // Then
    verify(trainerWorkloadRepository, times(1))
        .findByUsernameAndYearAndMonth(any(), anyInt(), any());
    verify(trainerWorkloadRepository, times(1)).save(any());
  }

  @Test
  void updateTrainersRecords_SubtractDurationAndDeleteRecord_Success() {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto(2024, 3, 120, ActionType.DELETE);
    TrainerWorkload trainerWorkload =
        TrainerWorkloadTestUtil.getTrainerWorkload1(2024, 3, 120);

    // When
    when(trainerWorkloadRepository.findByUsernameAndYearAndMonth(any(), anyInt(), any()))
        .thenReturn(Optional.of(trainerWorkload));
    doNothing().when(trainerWorkloadRepository).delete(any());

    trainerWorkloadService.updateTrainersWorkload(Collections.singletonList(trainerWorkloadUpdateDto));

    // Then
    verify(trainerWorkloadRepository, times(1))
        .findByUsernameAndYearAndMonth(any(), anyInt(), any());
    verify(trainerWorkloadRepository, times(1)).delete(any());
  }
}
