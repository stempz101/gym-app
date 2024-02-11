package com.epam.gymapp.test.utils;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.model.TrainingType;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class TrainingTestUtil {

  public static final long TEST_TRAINING_ID_1 = 1;
  public static final long TEST_TRAINING_TRAINEE_ID_1 = 2;
  public static final long TEST_TRAINING_TRAINER_ID_1 = 1;
  public static final String TEST_TRAINING_NAME_1 = "Cross #1";
  public static final TrainingType TEST_TRAINING_TYPE_1 = new TrainingType("CrossFit");
  public static final LocalDate TEST_TRAINING_DATE_1 = LocalDate.of(2024, 2, 6);
  public static final Duration TEST_TRAINING_DURATION_1 = Duration.ofHours(1);

  public static final long TEST_TRAINING_ID_2 = 2;
  public static final long TEST_TRAINING_TRAINEE_ID_2 = 3;
  public static final long TEST_TRAINING_TRAINER_ID_2 = 2;
  public static final String TEST_TRAINING_NAME_2 = "Functional #1";
  public static final TrainingType TEST_TRAINING_TYPE_2 = new TrainingType("Functional");
  public static final LocalDate TEST_TRAINING_DATE_2 = LocalDate.of(2024, 5, 1);
  public static final Duration TEST_TRAINING_DURATION_2 = Duration.ofHours(1).plusMinutes(30);

  public static final long TEST_TRAINING_ID_3 = 3;
  public static final long TEST_TRAINING_TRAINEE_ID_3 = 1;
  public static final long TEST_TRAINING_TRAINER_ID_3 = 3;
  public static final String TEST_TRAINING_NAME_3 = "Strength #1";
  public static final TrainingType TEST_TRAINING_TYPE_3 = new TrainingType("Strength");
  public static final LocalDate TEST_TRAINING_DATE_3 = LocalDate.of(2024, 5, 23);
  public static final Duration TEST_TRAINING_DURATION_3 = Duration.ofHours(1).plusMinutes(10);

  public static Training getTraining1() {
    return Training.builder()
        .id(TEST_TRAINING_ID_1)
        .traineeId(TEST_TRAINING_TRAINEE_ID_1)
        .trainerId(TEST_TRAINING_TRAINER_ID_1)
        .name(TEST_TRAINING_NAME_1)
        .type(TEST_TRAINING_TYPE_1)
        .date(TEST_TRAINING_DATE_1)
        .duration(TEST_TRAINING_DURATION_1)
        .build();
  }

  public static Training getTraining2() {
    return Training.builder()
        .id(TEST_TRAINING_ID_2)
        .traineeId(TEST_TRAINING_TRAINEE_ID_2)
        .trainerId(TEST_TRAINING_TRAINER_ID_2)
        .name(TEST_TRAINING_NAME_2)
        .type(TEST_TRAINING_TYPE_2)
        .date(TEST_TRAINING_DATE_2)
        .duration(TEST_TRAINING_DURATION_2)
        .build();
  }

  public static Training getTraining3() {
    return Training.builder()
        .id(TEST_TRAINING_ID_3)
        .traineeId(TEST_TRAINING_TRAINEE_ID_3)
        .trainerId(TEST_TRAINING_TRAINER_ID_3)
        .name(TEST_TRAINING_NAME_3)
        .type(TEST_TRAINING_TYPE_3)
        .date(TEST_TRAINING_DATE_3)
        .duration(TEST_TRAINING_DURATION_3)
        .build();
  }

  public static Training getTraining1FromTrainingCreateDto() {
    return Training.builder()
        .traineeId(TEST_TRAINING_TRAINEE_ID_1)
        .trainerId(TEST_TRAINING_TRAINER_ID_1)
        .name(TEST_TRAINING_NAME_1)
        .type(TEST_TRAINING_TYPE_1)
        .date(TEST_TRAINING_DATE_1)
        .duration(TEST_TRAINING_DURATION_1)
        .build();
  }

  public static List<Training> getTrainings() {
    return List.of(
        getTraining1(),
        getTraining2(),
        getTraining3()
    );
  }

  public static TrainingCreateDto getTrainingCreateDto1() {
    return TrainingCreateDto.builder()
        .traineeId(TEST_TRAINING_TRAINEE_ID_1)
        .trainerId(TEST_TRAINING_TRAINER_ID_1)
        .name(TEST_TRAINING_NAME_1)
        .type(TEST_TRAINING_TYPE_1)
        .date(TEST_TRAINING_DATE_1)
        .duration(TEST_TRAINING_DURATION_1)
        .build();
  }
}
