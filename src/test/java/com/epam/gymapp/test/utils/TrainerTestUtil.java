package com.epam.gymapp.test.utils;

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.TrainingType;
import java.util.List;

public class TrainerTestUtil {

  public static final String TEST_TRAINER_FIRST_NAME = "John";
  public static final String TEST_TRAINER_LAST_NAME = "Doe";
  public static final boolean TEST_TRAINER_IS_ACTIVE = true;

  public static final long TEST_TRAINER_USER_ID_1 = 1;
  public static final String TEST_TRAINER_USERNAME_1 = "John.Doe";
  public static final char[] TEST_TRAINER_PASSWORD_1 = "zpxZzJQ3gv".toCharArray();
  public static final TrainingType TEST_TRAINER_SPECIALIZATION_1 = new TrainingType("Bodybuilding");

  public static final long TEST_TRAINER_USER_ID_2 = 2;
  public static final String TEST_TRAINER_USERNAME_2 = "John.Doe2";
  public static final char[] TEST_TRAINER_PASSWORD_2 = "94c7sHTeY0".toCharArray();
  public static final TrainingType TEST_TRAINER_SPECIALIZATION_2 = new TrainingType("CrossFit");

  public static final long TEST_TRAINER_USER_ID_3 = 3;
  public static final String TEST_TRAINER_FIRST_NAME_3 = "Daniel";
  public static final String TEST_TRAINER_LAST_NAME_3 = "Thompson";
  public static final String TEST_TRAINER_USERNAME_3 = "Daniel.Thompson";
  public static final char[] TEST_TRAINER_PASSWORD_3 = "ZKj9nuryNC".toCharArray();
  public static final TrainingType TEST_TRAINER_SPECIALIZATION_3 = new TrainingType("Strength Training");

  public static Trainer getTrainer1() {
    return Trainer.builder()
        .userId(TEST_TRAINER_USER_ID_1)
        .firstName(TEST_TRAINER_FIRST_NAME)
        .lastName(TEST_TRAINER_LAST_NAME)
        .username(TEST_TRAINER_USERNAME_1)
        .password(TEST_TRAINER_PASSWORD_1)
        .specialization(TEST_TRAINER_SPECIALIZATION_1)
        .isActive(TEST_TRAINER_IS_ACTIVE)
        .build();
  }

  public static Trainer getTrainer2() {
    return Trainer.builder()
        .userId(TEST_TRAINER_USER_ID_2)
        .firstName(TEST_TRAINER_FIRST_NAME)
        .lastName(TEST_TRAINER_LAST_NAME)
        .username(TEST_TRAINER_USERNAME_2)
        .password(TEST_TRAINER_PASSWORD_2)
        .specialization(TEST_TRAINER_SPECIALIZATION_2)
        .isActive(TEST_TRAINER_IS_ACTIVE)
        .build();
  }

  public static Trainer getTrainer3() {
    return Trainer.builder()
        .userId(TEST_TRAINER_USER_ID_3)
        .firstName(TEST_TRAINER_FIRST_NAME_3)
        .lastName(TEST_TRAINER_LAST_NAME_3)
        .username(TEST_TRAINER_USERNAME_3)
        .password(TEST_TRAINER_PASSWORD_3)
        .specialization(TEST_TRAINER_SPECIALIZATION_3)
        .isActive(TEST_TRAINER_IS_ACTIVE)
        .build();
  }

  public static Trainer getTrainer1FromTrainerCreateDto() {
    return Trainer.builder()
        .firstName(TEST_TRAINER_FIRST_NAME)
        .lastName(TEST_TRAINER_LAST_NAME)
        .specialization(TEST_TRAINER_SPECIALIZATION_1)
        .build();
  }

  public static Trainer getUpdatedTrainer1() {
    return Trainer.builder()
        .userId(TEST_TRAINER_USER_ID_1)
        .firstName(TEST_TRAINER_FIRST_NAME_3)
        .lastName(TEST_TRAINER_LAST_NAME_3)
        .username(TEST_TRAINER_USERNAME_3)
        .password(TEST_TRAINER_PASSWORD_3)
        .specialization(TEST_TRAINER_SPECIALIZATION_3)
        .isActive(TEST_TRAINER_IS_ACTIVE)
        .build();
  }

  public static List<Trainer> getTrainers() {
    return List.of(
        getTrainer1(),
        getTrainer2(),
        getTrainer3()
    );
  }

  public static TrainerCreateDto getTrainerCreateDto1() {
    return TrainerCreateDto.builder()
        .firstName(TEST_TRAINER_FIRST_NAME)
        .lastName(TEST_TRAINER_LAST_NAME)
        .specialization(TEST_TRAINER_SPECIALIZATION_1)
        .build();
  }
}
