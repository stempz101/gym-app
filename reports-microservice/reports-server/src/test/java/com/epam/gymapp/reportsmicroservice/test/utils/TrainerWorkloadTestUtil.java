package com.epam.gymapp.reportsmicroservice.test.utils;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import java.time.LocalDate;
import java.time.Month;

public class TrainerWorkloadTestUtil {

  public static final long TEST_TRAINER_ID_1 = 1;
  public static final String TEST_TRAINER_USERNAME_1 = "John.Doe";
  public static final String TEST_TRAINER_FIRST_NAME_1 = "John";
  public static final String TEST_TRAINER_LAST_NAME_1 = "Doe";
  public static final boolean TEST_TRAINER_IS_ACTIVE_1 = true;

  public static final long TEST_TRAINER_ID_2 = 2;
  public static final String TEST_TRAINER_USERNAME_2 = "Sam.Wilson";
  public static final String TEST_TRAINER_FIRST_NAME_2 = "Sam";
  public static final String TEST_TRAINER_LAST_NAME_2 = "Wilson";
  public static final boolean TEST_TRAINER_IS_ACTIVE_2 = true;

  public static TrainerWorkload getTrainerWorkload1(int year, int month, long duration) {
    return TrainerWorkload.builder()
        .id(TEST_TRAINER_ID_1)
        .username(TEST_TRAINER_USERNAME_1)
        .firstName(TEST_TRAINER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_LAST_NAME_1)
        .isActive(TEST_TRAINER_IS_ACTIVE_1)
        .year(year)
        .month(Month.of(month))
        .duration(duration)
        .build();
  }

  public static TrainerWorkload getTrainerWorkload2(int year, int month, long duration) {
    return TrainerWorkload.builder()
        .id(TEST_TRAINER_ID_2)
        .username(TEST_TRAINER_USERNAME_2)
        .firstName(TEST_TRAINER_FIRST_NAME_2)
        .lastName(TEST_TRAINER_LAST_NAME_2)
        .isActive(TEST_TRAINER_IS_ACTIVE_2)
        .year(year)
        .month(Month.of(month))
        .duration(duration)
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

  public static TrainerWorkloadUpdateDto getTrainerWorkloadUpdateDto(
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
