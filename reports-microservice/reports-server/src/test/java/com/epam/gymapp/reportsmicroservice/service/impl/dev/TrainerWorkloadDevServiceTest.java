package com.epam.gymapp.reportsmicroservice.service.impl.dev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.mapper.TrainerWorkloadMapper;
import com.epam.gymapp.reportsmicroservice.model.MonthSummary;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.model.YearSummary;
import com.epam.gymapp.reportsmicroservice.repository.dev.TrainerWorkloadMongoRepository;
import com.epam.gymapp.reportsmicroservice.test.utils.TrainerWorkloadTestUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadDevServiceTest {

  @InjectMocks
  private TrainerWorkloadDevService trainerWorkloadService;

  @Mock
  private TrainerWorkloadMongoRepository trainerWorkloadRepository;

  @Mock
  private TrainerWorkloadMapper trainerWorkloadMapper;

  @Test
  void updateTrainersRecords_CreateNewRecord_Success() {
    // Given
    int year = 2024;
    int month = 3;
    long trainingDuration = 120;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(year, month, trainingDuration, ActionType.ADD);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();
    TrainerWorkload expectedResult = TrainerWorkloadTestUtil
        .getTrainerWorkload1(year, month, trainingDuration);

    // When
    when(trainerWorkloadRepository.findById(trainerWorkloadUpdateDto.getUsername()))
        .thenReturn(Optional.empty());
    when(trainerWorkloadMapper.toTrainerWorkload(trainerWorkloadUpdateDto)).thenReturn(trainerWorkload);
    when(trainerWorkloadRepository.save(expectedResult)).thenReturn(expectedResult);

    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    // Then
    verify(trainerWorkloadRepository, times(1)).findById(anyString());
    verify(trainerWorkloadMapper, times(1))
        .toTrainerWorkload(any(TrainerWorkloadUpdateDto.class));
    verify(trainerWorkloadRepository, times(1)).save(any(TrainerWorkload.class));

    assertEquals(expectedResult, trainerWorkload);
  }

  @Test
  void updateTrainersRecords_CreateNewYearAndNewMonthForExistingRecord_Success() {
    // Given
    int existingYear = 2024;
    int existingMonth = 3;
    long existingTrainingDuration = 120L;

    int newYear = 2025;
    int newMonth = 5;
    long newTrainingDuration = 60L;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(newYear, newMonth, newTrainingDuration, ActionType.ADD);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth, existingTrainingDuration);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    List<YearSummary> years = new ArrayList<>();
    List<MonthSummary> months1 = new ArrayList<>();
    List<MonthSummary> months2 = new ArrayList<>();

    months1.add(new MonthSummary(existingMonth, existingTrainingDuration));
    months2.add(new MonthSummary(newMonth, newTrainingDuration));

    years.add(new YearSummary(existingYear, months1));
    years.add(new YearSummary(newYear, months2));

    expectedResult.setYears(years);

    // When
    when(trainerWorkloadRepository.findById(trainerWorkloadUpdateDto.getUsername()))
        .thenReturn(Optional.of(existingTrainerWorkload));
    when(trainerWorkloadRepository.save(expectedResult)).thenReturn(expectedResult);

    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    // Then
    verifyNoInteractions(trainerWorkloadMapper);
    verify(trainerWorkloadRepository, times(1)).findById(anyString());
    verify(trainerWorkloadRepository, times(1)).save(any(TrainerWorkload.class));

    assertEquals(expectedResult, existingTrainerWorkload);
  }

  @Test
  void updateTrainersRecords_CreateNewMonthForExistingYearInRecord_Success() {
    // Given
    int existingYear = 2024;
    int existingMonth = 3;
    long existingTrainingDuration = 120L;

    int newMonth = 5;
    long newTrainingDuration = 60L;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(existingYear, newMonth, newTrainingDuration, ActionType.ADD);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth, existingTrainingDuration);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    List<YearSummary> years = new ArrayList<>();
    List<MonthSummary> months1 = new ArrayList<>();

    months1.add(new MonthSummary(existingMonth, existingTrainingDuration));
    months1.add(new MonthSummary(newMonth, newTrainingDuration));

    years.add(new YearSummary(existingYear, months1));

    expectedResult.setYears(years);

    // When
    when(trainerWorkloadRepository.findById(trainerWorkloadUpdateDto.getUsername()))
        .thenReturn(Optional.of(existingTrainerWorkload));
    when(trainerWorkloadRepository.save(expectedResult)).thenReturn(expectedResult);

    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    // Then
    verifyNoInteractions(trainerWorkloadMapper);
    verify(trainerWorkloadRepository, times(1)).findById(anyString());
    verify(trainerWorkloadRepository, times(1)).save(any(TrainerWorkload.class));

    assertEquals(expectedResult, existingTrainerWorkload);
  }

  @Test
  void updateTrainersRecords_AddDurationToExistingMonthOfRecord_Success() {
    // Given
    int existingYear = 2024;
    int existingMonth = 3;
    long existingTrainingDuration = 120L;

    long trainingDurationToAdd = 60L;
    long updatedTrainingDuration = existingTrainingDuration + trainingDurationToAdd;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(existingYear, existingMonth,
            trainingDurationToAdd, ActionType.ADD);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth, existingTrainingDuration);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    List<YearSummary> years = new ArrayList<>();
    List<MonthSummary> months1 = new ArrayList<>();

    months1.add(new MonthSummary(existingMonth, updatedTrainingDuration));
    years.add(new YearSummary(existingYear, months1));

    expectedResult.setYears(years);

    // When
    when(trainerWorkloadRepository.findById(trainerWorkloadUpdateDto.getUsername()))
        .thenReturn(Optional.of(existingTrainerWorkload));
    when(trainerWorkloadRepository.save(expectedResult)).thenReturn(expectedResult);

    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    // Then
    verifyNoInteractions(trainerWorkloadMapper);
    verify(trainerWorkloadRepository, times(1)).findById(anyString());
    verify(trainerWorkloadRepository, times(1)).save(any(TrainerWorkload.class));

    assertEquals(expectedResult, existingTrainerWorkload);
  }

  @Test
  void updateTrainersRecords_SubtractDurationFromExistingMonthOfRecord_Success() {
    // Given
    int existingYear = 2024;
    int existingMonth = 3;
    long existingTrainingDuration = 120L;

    long trainingDurationToSubtract = 60L;
    long updatedTrainingDuration = existingTrainingDuration - trainingDurationToSubtract;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(existingYear, existingMonth,
            trainingDurationToSubtract, ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth, existingTrainingDuration);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    List<YearSummary> years = new ArrayList<>();
    List<MonthSummary> months1 = new ArrayList<>();

    months1.add(new MonthSummary(existingMonth, updatedTrainingDuration));
    years.add(new YearSummary(existingYear, months1));

    expectedResult.setYears(years);

    // When
    when(trainerWorkloadRepository.findById(trainerWorkloadUpdateDto.getUsername()))
        .thenReturn(Optional.of(existingTrainerWorkload));
    when(trainerWorkloadRepository.save(expectedResult)).thenReturn(expectedResult);

    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    // Then
    verifyNoInteractions(trainerWorkloadMapper);
    verify(trainerWorkloadRepository, times(1)).findById(anyString());
    verify(trainerWorkloadRepository, times(1)).save(any(TrainerWorkload.class));

    assertEquals(expectedResult, existingTrainerWorkload);
  }

  @Test
  void updateTrainersRecords_RemoveMonthFromExistingYearOfRecord_Success() {
    // Given
    int existingYear = 2024;
    int existingMonth = 3;
    long existingTrainingDuration = 120L;

    int removingMonth = 5;
    long removingTrainingDuration = 60L;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(existingYear, removingMonth, removingTrainingDuration,
            ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    List<YearSummary> years = new ArrayList<>();
    List<MonthSummary> months = new ArrayList<>();

    months.add(new MonthSummary(existingMonth, existingTrainingDuration));
    months.add(new MonthSummary(removingMonth, removingTrainingDuration));

    years.add(new YearSummary(existingYear, months));

    existingTrainerWorkload.setYears(years);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth, existingTrainingDuration);

    // When
    when(trainerWorkloadRepository.findById(trainerWorkloadUpdateDto.getUsername()))
        .thenReturn(Optional.of(existingTrainerWorkload));
    when(trainerWorkloadRepository.save(expectedResult)).thenReturn(expectedResult);

    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    // Then
    verifyNoInteractions(trainerWorkloadMapper);
    verify(trainerWorkloadRepository, times(1)).findById(anyString());
    verify(trainerWorkloadRepository, times(1)).save(any(TrainerWorkload.class));

    assertEquals(expectedResult, existingTrainerWorkload);
  }

  @Test
  void updateTrainersRecords_RemoveYearFromExistingRecord_Success() {
    // Given
    int existingYear = 2024;
    int existingMonth = 3;
    long existingTrainingDuration = 120L;

    int removingYear = 2025;
    int removingMonth = 5;
    long removingTrainingDuration = 60L;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(removingYear, removingMonth, removingTrainingDuration,
            ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    List<YearSummary> years = new ArrayList<>();
    List<MonthSummary> months1 = new ArrayList<>();
    List<MonthSummary> months2 = new ArrayList<>();

    months1.add(new MonthSummary(existingMonth, existingTrainingDuration));
    months2.add(new MonthSummary(removingMonth, removingTrainingDuration));

    years.add(new YearSummary(existingYear, months1));
    years.add(new YearSummary(removingYear, months2));

    existingTrainerWorkload.setYears(years);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth, existingTrainingDuration);

    // When
    when(trainerWorkloadRepository.findById(trainerWorkloadUpdateDto.getUsername()))
        .thenReturn(Optional.of(existingTrainerWorkload));
    when(trainerWorkloadRepository.save(expectedResult)).thenReturn(expectedResult);

    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    // Then
    verifyNoInteractions(trainerWorkloadMapper);
    verify(trainerWorkloadRepository, times(1)).findById(anyString());
    verify(trainerWorkloadRepository, times(1)).save(any(TrainerWorkload.class));

    assertEquals(expectedResult, existingTrainerWorkload);
  }

  @Test
  void updateTrainersRecords_RemoveRecord_Success() {
    // Given
    int year = 2024;
    int month = 3;
    long trainingDuration = 120;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(year, month, trainingDuration, ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(year, month, trainingDuration);

    // When
    when(trainerWorkloadRepository.findById(trainerWorkloadUpdateDto.getUsername()))
        .thenReturn(Optional.of(trainerWorkload));
    doNothing().when(trainerWorkloadRepository).deleteById(trainerWorkload.getUsername());

    trainerWorkloadService.updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    // Then
    verifyNoInteractions(trainerWorkloadMapper);
    verify(trainerWorkloadRepository, times(1)).findById(anyString());
    verify(trainerWorkloadRepository, times(1)).deleteById(anyString());
  }
}
