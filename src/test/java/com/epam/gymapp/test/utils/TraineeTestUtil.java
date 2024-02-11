package com.epam.gymapp.test.utils;

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.model.Trainee;
import java.time.LocalDate;
import java.util.List;

public class TraineeTestUtil {

  public static final String TEST_TRAINEE_FIRST_NAME = "Michael";
  public static final String TEST_TRAINEE_LAST_NAME = "Patel";
  public static final boolean TEST_TRAINEE_IS_ACTIVE = true;

  public static final long TEST_TRAINEE_USER_ID_1 = 1;
  public static final String TEST_TRAINEE_USERNAME_1 = "Michael.Patel";
  public static final char[] TEST_TRAINEE_PASSWORD_1 = "uTlOfjgZg2".toCharArray();
  public static final LocalDate TEST_TRAINEE_DATE_OF_BIRTH_1 = LocalDate.of(2001, 7, 9);
  public static final String TEST_TRAINEE_ADDRESS_1 = "Deribasovska St, 1, Odesa";

  public static final long TEST_TRAINEE_USER_ID_2 = 2;
  public static final String TEST_TRAINEE_USERNAME_2 = "Michael.Patel3";
  public static final char[] TEST_TRAINEE_PASSWORD_2 = "mVsYvgvjmQ".toCharArray();
  public static final LocalDate TEST_TRAINEE_DATE_OF_BIRTH_2 = LocalDate.of(1999, 9, 5);
  public static final String TEST_TRAINEE_ADDRESS_2 = "Kanatna St, 4, Odesa";

  public static final long TEST_TRAINEE_USER_ID_3 = 3;
  public static final String TEST_TRAINEE_FIRST_NAME_3 = "Ashley";
  public static final String TEST_TRAINEE_LAST_NAME_3 = "Wilson";
  public static final String TEST_TRAINEE_USERNAME_3 = "Ashley.Wilson";
  public static final char[] TEST_TRAINEE_PASSWORD_3 = "RQBNpYJ3Yb".toCharArray();
  public static final LocalDate TEST_TRAINEE_DATE_OF_BIRTH_3 = LocalDate.of(1996, 10, 8);
  public static final String TEST_TRAINEE_ADDRESS_3 = "Preobrazhens'ka St, 33, Odesa";

  public static Trainee getTrainee1() {
    return Trainee.builder()
        .userId(TEST_TRAINEE_USER_ID_1)
        .firstName(TEST_TRAINEE_FIRST_NAME)
        .lastName(TEST_TRAINEE_LAST_NAME)
        .username(TEST_TRAINEE_USERNAME_1)
        .password(TEST_TRAINEE_PASSWORD_1)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .isActive(TEST_TRAINEE_IS_ACTIVE)
        .build();
  }

  public static Trainee getTrainee2() {
    return Trainee.builder()
        .userId(TEST_TRAINEE_USER_ID_2)
        .firstName(TEST_TRAINEE_FIRST_NAME)
        .lastName(TEST_TRAINEE_LAST_NAME)
        .username(TEST_TRAINEE_USERNAME_2)
        .password(TEST_TRAINEE_PASSWORD_2)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_2)
        .address(TEST_TRAINEE_ADDRESS_2)
        .isActive(TEST_TRAINEE_IS_ACTIVE)
        .build();
  }

  public static Trainee getTrainee3() {
    return Trainee.builder()
        .userId(TEST_TRAINEE_USER_ID_3)
        .firstName(TEST_TRAINEE_FIRST_NAME_3)
        .lastName(TEST_TRAINEE_LAST_NAME_3)
        .username(TEST_TRAINEE_USERNAME_3)
        .password(TEST_TRAINEE_PASSWORD_3)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_3)
        .address(TEST_TRAINEE_ADDRESS_3)
        .isActive(TEST_TRAINEE_IS_ACTIVE)
        .build();
  }

  public static Trainee getTrainee1FromTraineeCreateDto() {
    return Trainee.builder()
        .firstName(TEST_TRAINEE_FIRST_NAME)
        .lastName(TEST_TRAINEE_LAST_NAME)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .build();
  }

  public static Trainee getUpdatedTrainee1() {
    return Trainee.builder()
        .userId(TEST_TRAINEE_USER_ID_1)
        .firstName(TEST_TRAINEE_FIRST_NAME_3)
        .lastName(TEST_TRAINEE_LAST_NAME_3)
        .username(TEST_TRAINEE_USERNAME_3)
        .password(TEST_TRAINEE_PASSWORD_3)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_3)
        .address(TEST_TRAINEE_ADDRESS_3)
        .isActive(TEST_TRAINEE_IS_ACTIVE)
        .build();
  }

  public static List<Trainee> getTrainees() {
    return List.of(
        getTrainee1(),
        getTrainee2(),
        getTrainee3()
    );
  }

  public static TraineeCreateDto getTraineeCreateDto1() {
    return TraineeCreateDto.builder()
        .firstName(TEST_TRAINEE_FIRST_NAME)
        .lastName(TEST_TRAINEE_LAST_NAME)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .build();
  }
}
