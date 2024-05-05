package com.epam.gymapp.reportsmicroservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.mapper.TrainerWorkloadMapper;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.repository.TrainerWorkloadRepository;
import com.epam.gymapp.reportsmicroservice.repository.custom.CustomTrainerWorkloadRepository;
import com.epam.gymapp.reportsmicroservice.test.utils.TrainerWorkloadTestUtil;
import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private CustomTrainerWorkloadRepository customTrainerWorkloadRepository;

  @Mock
  private TrainerWorkloadMapper trainerWorkloadMapper;

  @Test
  void retrieveTrainersWorkloadForMonth_Success() {
    // Given
    int year = 2024;
    Month month = Month.of(4);
    String firstName = TrainerWorkloadTestUtil.TEST_TRAINER_FIRST_NAME_2;
    String lastName = TrainerWorkloadTestUtil.TEST_TRAINER_LAST_NAME_2;
    long trainingDuration = 120;

    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload2(year, month.getValue(), trainingDuration);
    List<TrainerWorkload> fetchedTrainerWorkload = Collections.singletonList(trainerWorkload);

    TrainerWorkloadDto trainerWorkloadDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadDto2(year, month.getValue(), trainingDuration);
    TrainerWorkloadDtoList expectedResult =
        new TrainerWorkloadDtoList(Collections.singletonList(trainerWorkloadDto));

    // When
    when(customTrainerWorkloadRepository
        .findAllByYearAndMonthAndFirstNameAndLastName(year, month, firstName, lastName))
        .thenReturn(fetchedTrainerWorkload);
    when(trainerWorkloadMapper.toTrainerWorkloadDto(trainerWorkload, year, month, trainingDuration))
        .thenReturn(trainerWorkloadDto);

    TrainerWorkloadDtoList result = trainerWorkloadService
        .retrieveTrainersWorkloadForMonth(year, month.getValue(), firstName, lastName);

    // Then
    verify(customTrainerWorkloadRepository, times(1))
        .findAllByYearAndMonthAndFirstNameAndLastName(year, month, firstName, lastName);
    verify(trainerWorkloadMapper, times(1))
        .toTrainerWorkloadDto(trainerWorkload, year, month, trainingDuration);

    assertThat(result, notNullValue());
    assertThat(result.getItems(), hasSize(expectedResult.getItems().size()));
    assertThat(result.getItems(), hasItems(trainerWorkloadDto));
  }

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
    Month existingMonth = Month.of(3);
    long existingTrainingDuration = 120L;

    int newYear = 2025;
    Month newMonth = Month.of(5);
    long newTrainingDuration = 60L;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(newYear, newMonth.getValue(), newTrainingDuration, ActionType.ADD);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth.getValue(), existingTrainingDuration);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();
    Map<Integer, Map<Month, Long>> yearsMap = new HashMap<>(existingTrainerWorkload.getYears());
    Map<Month, Long> months = new HashMap<>();
    months.put(newMonth, newTrainingDuration);
    yearsMap.put(newYear, months);
    expectedResult.setYears(yearsMap);

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
    Month existingMonth = Month.of(3);
    long existingTrainingDuration = 120L;

    Month newMonth = Month.of(5);
    long newTrainingDuration = 60L;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(existingYear, newMonth.getValue(), newTrainingDuration, ActionType.ADD);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth.getValue(), existingTrainingDuration);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();
    Map<Integer, Map<Month, Long>> yearsMap = new HashMap<>(
        existingTrainerWorkload.getYears());
    Map<Month, Long> months = new HashMap<>(existingTrainerWorkload.getYears().get(existingYear));
    months.put(newMonth, newTrainingDuration);
    yearsMap.put(existingYear, months);
    expectedResult.setYears(yearsMap);

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
    Month existingMonth = Month.of(3);
    long existingTrainingDuration = 120L;

    long trainingDurationToAdd = 60L;
    long updatedTrainingDuration = existingTrainingDuration + trainingDurationToAdd;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(existingYear, existingMonth.getValue(),
            trainingDurationToAdd, ActionType.ADD);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth.getValue(), existingTrainingDuration);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();
    Map<Integer, Map<Month, Long>> yearsMap = new HashMap<>(
        existingTrainerWorkload.getYears());
    Map<Month, Long> months = new HashMap<>(existingTrainerWorkload.getYears().get(existingYear));
    months.put(existingMonth, updatedTrainingDuration);
    yearsMap.put(existingYear, months);
    expectedResult.setYears(yearsMap);

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
    Month existingMonth = Month.of(3);
    long existingTrainingDuration = 120L;

    long trainingDurationToSubtract = 60L;
    long updatedTrainingDuration = existingTrainingDuration - trainingDurationToSubtract;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(existingYear, existingMonth.getValue(),
            trainingDurationToSubtract, ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth.getValue(), existingTrainingDuration);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();
    Map<Integer, Map<Month, Long>> yearsMap = new HashMap<>(
        existingTrainerWorkload.getYears());
    Map<Month, Long> months = new HashMap<>(existingTrainerWorkload.getYears().get(existingYear));
    months.put(existingMonth, updatedTrainingDuration);
    yearsMap.put(existingYear, months);
    expectedResult.setYears(yearsMap);

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
    Month existingMonth = Month.of(3);
    long existingTrainingDuration = 120L;

    Month removingMonth = Month.of(5);
    long removingTrainingDuration = 60L;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(existingYear, removingMonth.getValue(), removingTrainingDuration,
            ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();
    Map<Month, Long> months = new HashMap<>();
    months.put(existingMonth, existingTrainingDuration);
    months.put(removingMonth, removingTrainingDuration);
    existingTrainerWorkload.getYears().put(existingYear, months);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth.getValue(), existingTrainingDuration);

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
    Month existingMonth = Month.of(3);
    long existingTrainingDuration = 120L;

    int removingYear = 2025;
    Month removingMonth = Month.of(5);
    long removingTrainingDuration = 60L;

    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = TrainerWorkloadTestUtil
        .getTrainerWorkloadUpdateDto1(removingYear, removingMonth.getValue(), removingTrainingDuration,
            ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(Collections.singletonList(trainerWorkloadUpdateDto));

    TrainerWorkload existingTrainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    Map<Month, Long> monthsOfExistingYear = new HashMap<>();
    monthsOfExistingYear.put(existingMonth, existingTrainingDuration);
    existingTrainerWorkload.getYears().put(existingYear, monthsOfExistingYear);

    Map<Month, Long> monthsOfRemovingYear = new HashMap<>();
    monthsOfRemovingYear.put(removingMonth, removingTrainingDuration);
    existingTrainerWorkload.getYears().put(removingYear, monthsOfRemovingYear);

    TrainerWorkload expectedResult = TrainerWorkloadTestUtil
        .getTrainerWorkload1(existingYear, existingMonth.getValue(), existingTrainingDuration);

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
