package com.epam.gymapp.test.utils;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.model.Training;
import java.time.LocalDate;
import java.util.List;

public class TrainingTestUtil {

  public static final long TEST_TRAINING_ID_1 = 1;
  public static final String TEST_TRAINING_NAME_1 = "Cross #1";
  public static final LocalDate TEST_TRAINING_DATE_1 = LocalDate.of(2024, 2, 6);
  public static final int TEST_TRAINING_DURATION_1 = 60;

  public static final long TEST_TRAINING_ID_2 = 2;
  public static final String TEST_TRAINING_NAME_2 = "Strength #1";
  public static final LocalDate TEST_TRAINING_DATE_2 = LocalDate.of(2024, 2, 6);
  public static final int TEST_TRAINING_DURATION_2 = 90;

  public static final long TEST_TRAINING_ID_3 = 3;
  public static final String TEST_TRAINING_NAME_3 = "Bodybuilding #1";
  public static final LocalDate TEST_TRAINING_DATE_3 = LocalDate.of(2024, 2, 7);
  public static final int TEST_TRAINING_DURATION_3 = 80;

  public static final long TEST_TRAINING_ID_4 = 4;
  public static final String TEST_TRAINING_NAME_4 = "Strength #2";
  public static final LocalDate TEST_TRAINING_DATE_4 = LocalDate.of(2024, 2, 7);
  public static final int TEST_TRAINING_DURATION_4 = 110;

  public static final long TEST_TRAINING_ID_5 = 5;
  public static final String TEST_TRAINING_NAME_5 = "Bodybuilding #2";
  public static final LocalDate TEST_TRAINING_DATE_5 = LocalDate.of(2024, 2, 10);
  public static final int TEST_TRAINING_DURATION_5 = 110;

  public static final long TEST_TRAINING_ID_6 = 6;
  public static final String TEST_TRAINING_NAME_6 = "Cross #2";
  public static final LocalDate TEST_TRAINING_DATE_6 = LocalDate.of(2024, 2, 12);
  public static final int TEST_TRAINING_DURATION_6 = 110;

  public static final long TEST_TRAINING_ID_7 = 7;
  public static final String TEST_TRAINING_NAME_7 = "Bodybuilding #3";
  public static final LocalDate TEST_TRAINING_DATE_7 = LocalDate.of(2024, 2, 13);
  public static final int TEST_TRAINING_DURATION_7 = 110;

  public static final String TEST_TRAINING_NAME_8 = "Bodybuilding #3";
  public static final LocalDate TEST_TRAINING_DATE_8 = LocalDate.of(2024, 2, 24);
  public static final int TEST_TRAINING_DURATION_8 = 120;

  public static Training getNewTraining8() {
    return Training.builder()
        .trainee(TraineeTestUtil.getTrainee1())
        .trainer(TrainerTestUtil.getTrainer1())
        .type(TrainingTypeTestUtil.getTrainingType1())
        .name(TEST_TRAINING_NAME_8)
        .date(TEST_TRAINING_DATE_8)
        .duration(TEST_TRAINING_DURATION_8)
        .build();
  }

  public static Training getTraining1() {
    return Training.builder()
        .id(TEST_TRAINING_ID_1)
        .trainee(TraineeTestUtil.getTrainee1())
        .trainer(TrainerTestUtil.getTrainer2())
        .type(TrainingTypeTestUtil.getTrainingType2())
        .name(TEST_TRAINING_NAME_1)
        .date(TEST_TRAINING_DATE_1)
        .duration(TEST_TRAINING_DURATION_1)
        .build();
  }

  public static Training getTraining2() {
    return Training.builder()
        .id(TEST_TRAINING_ID_2)
        .trainee(TraineeTestUtil.getTrainee2())
        .trainer(TrainerTestUtil.getTrainer3())
        .type(TrainingTypeTestUtil.getTrainingType3())
        .name(TEST_TRAINING_NAME_2)
        .date(TEST_TRAINING_DATE_2)
        .duration(TEST_TRAINING_DURATION_2)
        .build();
  }

  public static Training getTraining3() {
    return Training.builder()
        .id(TEST_TRAINING_ID_3)
        .trainee(TraineeTestUtil.getTrainee3())
        .trainer(TrainerTestUtil.getTrainer1())
        .type(TrainingTypeTestUtil.getTrainingType1())
        .name(TEST_TRAINING_NAME_3)
        .date(TEST_TRAINING_DATE_3)
        .duration(TEST_TRAINING_DURATION_3)
        .build();
  }

  public static Training getTraining4() {
    return Training.builder()
        .id(TEST_TRAINING_ID_4)
        .trainee(TraineeTestUtil.getTrainee4())
        .trainer(TrainerTestUtil.getTrainer3())
        .type(TrainingTypeTestUtil.getTrainingType3())
        .name(TEST_TRAINING_NAME_4)
        .date(TEST_TRAINING_DATE_4)
        .duration(TEST_TRAINING_DURATION_4)
        .build();
  }

  public static Training getTraining5() {
    return Training.builder()
        .id(TEST_TRAINING_ID_5)
        .trainee(TraineeTestUtil.getTrainee2())
        .trainer(TrainerTestUtil.getTrainer1())
        .type(TrainingTypeTestUtil.getTrainingType1())
        .name(TEST_TRAINING_NAME_5)
        .date(TEST_TRAINING_DATE_5)
        .duration(TEST_TRAINING_DURATION_5)
        .build();
  }

  public static Training getTraining6() {
    return Training.builder()
        .id(TEST_TRAINING_ID_6)
        .trainee(TraineeTestUtil.getTrainee3())
        .trainer(TrainerTestUtil.getTrainer2())
        .type(TrainingTypeTestUtil.getTrainingType2())
        .name(TEST_TRAINING_NAME_6)
        .date(TEST_TRAINING_DATE_6)
        .duration(TEST_TRAINING_DURATION_6)
        .build();
  }

  public static Training getTraining7() {
    return Training.builder()
        .id(TEST_TRAINING_ID_7)
        .trainee(TraineeTestUtil.getTrainee3())
        .trainer(TrainerTestUtil.getTrainer4())
        .type(TrainingTypeTestUtil.getTrainingType1())
        .name(TEST_TRAINING_NAME_7)
        .date(TEST_TRAINING_DATE_7)
        .duration(TEST_TRAINING_DURATION_7)
        .build();
  }

  public static List<Training> getTrainings() {
    return List.of(getTraining1(), getTraining2());
  }

  public static TrainingCreateDto getTrainingCreateDto1() {
    return TrainingCreateDto.builder()
        .traineeUsername(UserTestUtil.TEST_TRAINEE_USER_USERNAME_1)
        .trainerUsername(UserTestUtil.TEST_TRAINER_USER_USERNAME_2)
        .name(TEST_TRAINING_NAME_1)
        .date(TEST_TRAINING_DATE_1)
        .duration(TEST_TRAINING_DURATION_1)
        .build();
  }

  public static Training getTraining1FromTrainingCreateDto() {
    return Training.builder()
        .name(TEST_TRAINING_NAME_1)
        .date(TEST_TRAINING_DATE_1)
        .duration(TEST_TRAINING_DURATION_1)
        .build();
  }

  public static TrainingInfoDto getTrainingInfoDto1() {
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1);
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_2, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_2);

    return TrainingInfoDto.builder()
        .name(TEST_TRAINING_NAME_1)
        .date(TEST_TRAINING_DATE_1)
        .duration(TEST_TRAINING_DURATION_1)
        .traineeName(traineeName)
        .trainerName(trainerName)
        .type(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_2)
        .build();
  }

  public static TrainingInfoDto getTrainingInfoDto2() {
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_2, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_2);
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3);

    return TrainingInfoDto.builder()
        .name(TEST_TRAINING_NAME_2)
        .date(TEST_TRAINING_DATE_2)
        .duration(TEST_TRAINING_DURATION_2)
        .traineeName(traineeName)
        .trainerName(trainerName)
        .type(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_3)
        .build();
  }

  public static TrainingInfoDto getTrainingInfoDto5() {
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_2, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_2);
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1);

    return TrainingInfoDto.builder()
        .name(TEST_TRAINING_NAME_5)
        .date(TEST_TRAINING_DATE_5)
        .duration(TEST_TRAINING_DURATION_5)
        .traineeName(traineeName)
        .trainerName(trainerName)
        .type(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_1)
        .build();
  }

  public static TrainingInfoDto getTrainingInfoDto6() {
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_3, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_3);
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_2, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_2);

    return TrainingInfoDto.builder()
        .name(TEST_TRAINING_NAME_6)
        .date(TEST_TRAINING_DATE_6)
        .duration(TEST_TRAINING_DURATION_6)
        .traineeName(traineeName)
        .trainerName(trainerName)
        .type(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_2)
        .build();
  }

  public static List<TrainingInfoDto> getTrainingInfoDtos() {
    return List.of(getTrainingInfoDto1(), getTrainingInfoDto2());
  }

  public static List<Training> getTrainingsOfTrainee2() {
    return List.of(getTraining2(), getTraining5());
  }

  public static List<Training> getTrainingsOfTrainer2() {
    return List.of(getTraining1(), getTraining6());
  }

  public static List<TrainingInfoDto> getTrainingInfoDtosOfTrainee2() {
    return List.of(getTrainingInfoDto2(), getTrainingInfoDto5());
  }

  public static List<TrainingInfoDto> getTrainingInfoDtosOfTrainer2() {
    return List.of(getTrainingInfoDto1(), getTrainingInfoDto6());
  }
}
