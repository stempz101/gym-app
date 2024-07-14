package com.epam.gymapp.reportsmicroservice.test.utils;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.model.MonthSummary;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.model.YearSummary;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrainerWorkloadTestUtil {

  public static final String TEST_TRAINER_USERNAME_1 = "John.Doe";
  public static final String TEST_TRAINER_FIRST_NAME_1 = "John";
  public static final String TEST_TRAINER_LAST_NAME_1 = "Doe";
  public static final boolean TEST_TRAINER_STATUS_1 = true;

  public static final String TEST_TRAINER_USERNAME_2 = "Sam.Wilson";
  public static final String TEST_TRAINER_FIRST_NAME_2 = "Sam";
  public static final String TEST_TRAINER_LAST_NAME_2 = "Wilson";
  public static final boolean TEST_TRAINER_STATUS_2 = true;

  public static TrainerWorkload getTrainerWorkload1() {
    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload1WithoutYears();

    List<YearSummary> years = new ArrayList<>();

    List<MonthSummary> months1 = new ArrayList<>();
    months1.add(new MonthSummary(3, 120L));
    months1.add(new MonthSummary(4, 170L));

    List<MonthSummary> months2 = new ArrayList<>();
    months2.add(new MonthSummary(4, 210L));
    months2.add(new MonthSummary(5, 140L));

    years.add(new YearSummary(2024, months1));
    years.add(new YearSummary(2025, months2));

    trainerWorkload.setYears(years);

    return trainerWorkload;
  }

  public static TrainerWorkload getTrainerWorkload2() {
    TrainerWorkload trainerWorkload = TrainerWorkloadTestUtil.getTrainerWorkload2WithoutYears();

    List<YearSummary> years = new ArrayList<>();

    List<MonthSummary> months1 = new ArrayList<>();
    months1.add(new MonthSummary(3, 230L));
    months1.add(new MonthSummary(4, 200L));

    List<MonthSummary> months2 = new ArrayList<>();
    months2.add(new MonthSummary(4, 190L));
    months2.add(new MonthSummary(5, 110L));

    years.add(new YearSummary(2024, months1));
    years.add(new YearSummary(2025, months2));

    trainerWorkload.setYears(years);

    return trainerWorkload;
  }

  public static TrainerWorkload getTrainerWorkload1(int year, int month, long duration) {
    List<YearSummary> years = new ArrayList<>();
    List<MonthSummary> months = new ArrayList<>();

    months.add(new MonthSummary(month, duration));
    years.add(new YearSummary(year, months));

    return TrainerWorkload.builder()
        .username(TEST_TRAINER_USERNAME_1)
        .firstName(TEST_TRAINER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_LAST_NAME_1)
        .status(String.valueOf(TEST_TRAINER_STATUS_1))
        .years(years)
        .build();
  }

  public static TrainerWorkload getTrainerWorkload1WithoutYears() {

    return TrainerWorkload.builder()
        .username(TEST_TRAINER_USERNAME_1)
        .firstName(TEST_TRAINER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_LAST_NAME_1)
        .status(String.valueOf(TEST_TRAINER_STATUS_1))
        .build();
  }

  public static TrainerWorkload getTrainerWorkload2WithoutYears() {

    return TrainerWorkload.builder()
        .username(TEST_TRAINER_USERNAME_2)
        .firstName(TEST_TRAINER_FIRST_NAME_2)
        .lastName(TEST_TRAINER_LAST_NAME_2)
        .status(String.valueOf(TEST_TRAINER_STATUS_2))
        .build();
  }

  public static TrainerWorkloadUpdateDto getTrainerWorkloadUpdateDto1(
      int year, int month, long duration, ActionType actionType
  ) {
    return TrainerWorkloadUpdateDto.builder()
        .username(TEST_TRAINER_USERNAME_1)
        .firstName(TEST_TRAINER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_LAST_NAME_1)
        .isActive(TEST_TRAINER_STATUS_1)
        .trainingDate(LocalDate.of(year, month, 1))
        .trainingDuration(duration)
        .actionType(actionType)
        .build();
  }
}
