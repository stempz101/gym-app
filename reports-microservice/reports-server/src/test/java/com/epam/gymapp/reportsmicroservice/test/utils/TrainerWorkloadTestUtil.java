package com.epam.gymapp.reportsmicroservice.test.utils;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class TrainerWorkloadTestUtil {

  public static final String TEST_TRAINER_USERNAME_1 = "John.Doe";
  public static final String TEST_TRAINER_FIRST_NAME_1 = "John";
  public static final String TEST_TRAINER_LAST_NAME_1 = "Doe";
  public static final boolean TEST_TRAINER_IS_ACTIVE_1 = true;

  public static final String TEST_TRAINER_USERNAME_2 = "Sam.Wilson";
  public static final String TEST_TRAINER_FIRST_NAME_2 = "Sam";
  public static final String TEST_TRAINER_LAST_NAME_2 = "Wilson";
  public static final boolean TEST_TRAINER_IS_ACTIVE_2 = true;

  public static TrainerWorkload getTrainerWorkload1() {
    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    Map<Integer, Map<Month, Long>> years = trainerWorkload.getYears();
    Map<Month, Long> months1 = new HashMap<>();
    Map<Month, Long> months2 = new HashMap<>();

    months1.put(Month.of(3), 120L);
    months1.put(Month.of(4), 170L);
    months2.put(Month.of(4), 210L);
    months2.put(Month.of(5), 140L);

    years.put(2024, months1);
    years.put(2025, months2);

    return trainerWorkload;
  }

  public static TrainerWorkload getTrainerWorkload2() {
    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload2WithoutYears();

    Map<Integer, Map<Month, Long>> years = trainerWorkload.getYears();
    Map<Month, Long> months1 = new HashMap<>();
    Map<Month, Long> months2 = new HashMap<>();

    months1.put(Month.of(3), 230L);
    months1.put(Month.of(4), 200L);
    months2.put(Month.of(4), 190L);
    months2.put(Month.of(5), 110L);

    years.put(2024, months1);
    years.put(2025, months2);

    return trainerWorkload;
  }

  public static TrainerWorkload getTrainerWorkload1(int year, int month, long duration) {
    Map<Integer, Map<Month, Long>> years = new HashMap<>();
    Map<Month, Long> months = new HashMap<>();

    months.put(Month.of(month), duration);
    years.put(year, months);

    return TrainerWorkload.builder()
        .username(TEST_TRAINER_USERNAME_1)
        .firstName(TEST_TRAINER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_LAST_NAME_1)
        .isActive(TEST_TRAINER_IS_ACTIVE_1)
        .years(years)
        .build();
  }

  public static TrainerWorkload getTrainerWorkload1WithoutYears() {

    return TrainerWorkload.builder()
        .username(TEST_TRAINER_USERNAME_1)
        .firstName(TEST_TRAINER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_LAST_NAME_1)
        .isActive(TEST_TRAINER_IS_ACTIVE_1)
        .build();
  }

  public static TrainerWorkload getTrainerWorkload2(int year, int month, long duration) {
    Map<Integer, Map<Month, Long>> years = new HashMap<>();
    Map<Month, Long> months = new HashMap<>();

    months.put(Month.of(month), duration);
    years.put(year, months);

    return TrainerWorkload.builder()
        .username(TEST_TRAINER_USERNAME_2)
        .firstName(TEST_TRAINER_FIRST_NAME_2)
        .lastName(TEST_TRAINER_LAST_NAME_2)
        .isActive(TEST_TRAINER_IS_ACTIVE_2)
        .years(years)
        .build();
  }

  public static TrainerWorkload getTrainerWorkload2WithoutYears() {

    return TrainerWorkload.builder()
        .username(TEST_TRAINER_USERNAME_2)
        .firstName(TEST_TRAINER_FIRST_NAME_2)
        .lastName(TEST_TRAINER_LAST_NAME_2)
        .isActive(TEST_TRAINER_IS_ACTIVE_2)
        .build();
  }

  public static TrainerWorkloadDto getTrainerWorkloadDto1(
      int year, int month, long duration
  ) {
    return TrainerWorkloadDto.builder()
        .username(TEST_TRAINER_USERNAME_1)
        .firstName(TEST_TRAINER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_LAST_NAME_1)
        .isActive(TEST_TRAINER_IS_ACTIVE_1)
        .year(year)
        .month(Month.of(month))
        .duration(duration)
        .build();
  }

  public static TrainerWorkloadDto getTrainerWorkloadDto2(
      int year, int month, long duration
  ) {
    return TrainerWorkloadDto.builder()
        .username(TEST_TRAINER_USERNAME_2)
        .firstName(TEST_TRAINER_FIRST_NAME_2)
        .lastName(TEST_TRAINER_LAST_NAME_2)
        .isActive(TEST_TRAINER_IS_ACTIVE_2)
        .year(year)
        .month(Month.of(month))
        .duration(duration)
        .build();
  }

  public static TrainerWorkloadUpdateDto getTrainerWorkloadUpdateDto1(
      int year, int month, long duration, ActionType actionType
  ) {
    return TrainerWorkloadUpdateDto.builder()
        .username(TEST_TRAINER_USERNAME_1)
        .firstName(TEST_TRAINER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_LAST_NAME_1)
        .isActive(TEST_TRAINER_IS_ACTIVE_1)
        .trainingDate(LocalDate.of(year, month, 1))
        .trainingDuration(duration)
        .actionType(actionType)
        .build();
  }
}
