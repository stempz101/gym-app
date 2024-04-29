package com.epam.gymapp.mainmicroservice.test.utils;

import com.epam.gymapp.mainmicroservice.dto.ChangePasswordDto;
import com.epam.gymapp.mainmicroservice.dto.JwtDto;
import com.epam.gymapp.mainmicroservice.dto.UserActivateDto;
import com.epam.gymapp.mainmicroservice.dto.UserCredentialsDto;
import com.epam.gymapp.mainmicroservice.model.User;

public class UserTestUtil {

  public static final long TEST_TRAINEE_USER_ID_1 = 1;
  public static final String TEST_TRAINEE_USER_FIRST_NAME_1 = "Michael";
  public static final String TEST_TRAINEE_USER_LAST_NAME_1 = "Patel";
  public static final String TEST_TRAINEE_USER_USERNAME_1 = "Michael.Patel";
  public static final String TEST_TRAINEE_USER_PASSWORD_1 = "uTlOfjgZg2";
  public static final char[] TEST_TRAINEE_USER_PASSWORD_CHAR_ARRAY_1 = TEST_TRAINEE_USER_PASSWORD_1.toCharArray();
  public static final char[] TEST_TRAINEE_USER_NEW_PASSWORD_CHAR_ARRAY_1 = "qwerty1234".toCharArray();
  public static final boolean TEST_TRAINEE_USER_IS_ACTIVE_1 = true;

  public static final long TEST_TRAINEE_USER_ID_2 = 2;
  public static final String TEST_TRAINEE_USER_FIRST_NAME_2 = "Michael";
  public static final String TEST_TRAINEE_USER_LAST_NAME_2 = "Patel";
  public static final String TEST_TRAINEE_USER_USERNAME_2 = "Michael.Patel1";
  public static final String TEST_TRAINEE_USER_PASSWORD_2 = "QZ2TWVlyFX";
  public static final char[] TEST_TRAINEE_USER_PASSWORD_CHAR_ARRAY_2 = TEST_TRAINEE_USER_PASSWORD_2.toCharArray();
  public static final boolean TEST_TRAINEE_USER_IS_ACTIVE_2 = true;

  public static final long TEST_TRAINEE_USER_ID_3 = 3;
  public static final String TEST_TRAINEE_USER_FIRST_NAME_3 = "Christopher";
  public static final String TEST_TRAINEE_USER_LAST_NAME_3 = "Lee";
  public static final String TEST_TRAINEE_USER_USERNAME_3 = "Christopher.Lee4";
  public static final String TEST_TRAINEE_USER_PASSWORD_3 = "mVsYvgvjmQ";
  public static final char[] TEST_TRAINEE_USER_PASSWORD_CHAR_ARRAY_3 = TEST_TRAINEE_USER_PASSWORD_3.toCharArray();
  public static final boolean TEST_TRAINEE_USER_IS_ACTIVE_3 = true;

  public static final long TEST_TRAINEE_USER_ID_4 = 4;
  public static final String TEST_TRAINEE_USER_FIRST_NAME_4 = "Sarah";
  public static final String TEST_TRAINEE_USER_LAST_NAME_4 = "Nguyen";
  public static final String TEST_TRAINEE_USER_USERNAME_4 = "Sarah.Nguyen";
  public static final String TEST_TRAINEE_USER_PASSWORD_4 = "RQBNpYJ3Yb";
  public static final char[] TEST_TRAINEE_USER_PASSWORD_CHAR_ARRAY_4 = TEST_TRAINEE_USER_PASSWORD_4.toCharArray();
  public static final boolean TEST_TRAINEE_USER_IS_ACTIVE_4 = true;

  public static final long TEST_TRAINER_USER_ID_1 = 5;
  public static final String TEST_TRAINER_USER_FIRST_NAME_1 = "John";
  public static final String TEST_TRAINER_USER_LAST_NAME_1 = "Doe";
  public static final String TEST_TRAINER_USER_USERNAME_1 = "John.Doe";
  public static final String TEST_TRAINER_USER_PASSWORD_1 = "zpxZzJQ3gv";
  public static final char[] TEST_TRAINER_USER_PASSWORD_CHAR_ARRAY_1 = TEST_TRAINER_USER_PASSWORD_1.toCharArray();
  public static final boolean TEST_TRAINER_USER_IS_ACTIVE_1 = true;

  public static final long TEST_TRAINER_USER_ID_2 = 6;
  public static final String TEST_TRAINER_USER_FIRST_NAME_2 = "Jessica";
  public static final String TEST_TRAINER_USER_LAST_NAME_2 = "Rodriguez";
  public static final String TEST_TRAINER_USER_USERNAME_2 = "Jessica.Rodriguez2";
  public static final String TEST_TRAINER_USER_PASSWORD_2 = "94c7sHTeY0";
  public static final char[] TEST_TRAINER_USER_PASSWORD_CHAR_ARRAY_2 = TEST_TRAINER_USER_PASSWORD_2.toCharArray();
  public static final boolean TEST_TRAINER_USER_IS_ACTIVE_2 = true;

  public static final long TEST_TRAINER_USER_ID_3 = 7;
  public static final String TEST_TRAINER_USER_FIRST_NAME_3 = "Daniel";
  public static final String TEST_TRAINER_USER_LAST_NAME_3 = "Thompson";
  public static final String TEST_TRAINER_USER_USERNAME_3 = "Daniel.Thompson";
  public static final String TEST_TRAINER_USER_PASSWORD_3 = "ZKj9nuryNC";
  public static final char[] TEST_TRAINER_USER_PASSWORD_CHAR_ARRAY_3 = TEST_TRAINER_USER_PASSWORD_3.toCharArray();
  public static final boolean TEST_TRAINER_USER_IS_ACTIVE_3 = true;

  public static final long TEST_TRAINER_USER_ID_4 = 8;
  public static final String TEST_TRAINER_USER_FIRST_NAME_4 = "Max";
  public static final String TEST_TRAINER_USER_LAST_NAME_4 = "Simons";
  public static final String TEST_TRAINER_USER_USERNAME_4 = "Max.Simons";
  public static final String TEST_TRAINER_USER_PASSWORD_4 = "YIUYdasd23";
  public static final char[] TEST_TRAINER_USER_PASSWORD_CHAR_ARRAY_4 = TEST_TRAINER_USER_PASSWORD_4.toCharArray();
  public static final boolean TEST_TRAINER_USER_IS_ACTIVE_4 = true;

  public static final long TEST_USER_ID_9 = 9;
  public static final String TEST_USER_FIRST_NAME_9 = "Rodrigo";
  public static final String TEST_USER_LAST_NAME_9 = "Jimenez";
  public static final String TEST_USER_USERNAME_9 = "Rodrigo.Jimenez";
  public static final String TEST_USER_PASSWORD_9 = "JLfgsh89aP";
  public static final char[] TEST_USER_PASSWORD_CHAR_ARRAY_9 = TEST_USER_PASSWORD_9.toCharArray();
  public static final boolean TEST_USER_IS_ACTIVE_9 = false;

  public static final String TEST_NEW_TRAINEE_FIRST_NAME_1 = "Will";
  public static final String TEST_NEW_TRAINEE_LAST_NAME_1 = "Smith";
  public static final String TEST_NEW_TRAINEE_USERNAME_1 = "Will.Smith";
  public static final String TEST_NEW_TRAINEE_PASSWORD_1 = "aiuyczl3fg";
  public static final char[] TEST_NEW_TRAINEE_PASSWORD_CHAR_ARRAY_1 = TEST_NEW_TRAINEE_PASSWORD_1.toCharArray();

  public static final String TEST_NEW_TRAINER_FIRST_NAME_1 = "Jack";
  public static final String TEST_NEW_TRAINER_LAST_NAME_1 = "Sparrow";
  public static final String TEST_NEW_TRAINER_USERNAME_1 = "Jack.Sparrow";
  public static final String TEST_NEW_TRAINER_PASSWORD_1 = "KujGAhsad3";
  public static final char[] TEST_NEW_TRAINER_PASSWORD_CHAR_ARRAY_1 = TEST_NEW_TRAINER_PASSWORD_1.toCharArray();

  public static final String TEST_NEW_TRAINER_FIRST_NAME_2 = "Jonathan";
  public static final String TEST_NEW_TRAINER_LAST_NAME_2 = "Joestar";
  public static final String TEST_NEW_TRAINER_USERNAME_2 = "Jonathan.Joestar";
  public static final String TEST_NEW_TRAINER_PASSWORD_2 = "oRA82hdTrA";
  public static final char[] TEST_NEW_TRAINER_PASSWORD_CHAR_ARRAY_2 = TEST_NEW_TRAINER_PASSWORD_2.toCharArray();

  public static User getNewTraineeUser1() {
    return User.builder()
        .firstName(TEST_NEW_TRAINEE_FIRST_NAME_1)
        .lastName(TEST_NEW_TRAINEE_LAST_NAME_1)
        .username(TEST_NEW_TRAINEE_USERNAME_1)
        .password(TEST_NEW_TRAINEE_PASSWORD_1)
        .build();
  }

  public static User getNewTrainerUser1() {
    return User.builder()
        .firstName(TEST_NEW_TRAINER_FIRST_NAME_1)
        .lastName(TEST_NEW_TRAINER_LAST_NAME_1)
        .username(TEST_NEW_TRAINER_USERNAME_1)
        .password(TEST_NEW_TRAINER_PASSWORD_1)
        .build();
  }

  public static User getNewTrainerUser2() {
    return User.builder()
        .firstName(TEST_NEW_TRAINER_FIRST_NAME_2)
        .lastName(TEST_NEW_TRAINER_LAST_NAME_2)
        .username(TEST_NEW_TRAINER_USERNAME_2)
        .password(TEST_NEW_TRAINER_PASSWORD_2)
        .build();
  }

  public static User getTraineeUser1() {
    return User.builder()
        .id(TEST_TRAINEE_USER_ID_1)
        .firstName(TEST_TRAINEE_USER_FIRST_NAME_1)
        .lastName(TEST_TRAINEE_USER_LAST_NAME_1)
        .username(TEST_TRAINEE_USER_USERNAME_1)
        .password(TEST_TRAINEE_USER_PASSWORD_1)
        .isActive(TEST_TRAINEE_USER_IS_ACTIVE_1)
        .build();
  }

  public static User getTraineeUser2() {
    return User.builder()
        .id(TEST_TRAINEE_USER_ID_2)
        .firstName(TEST_TRAINEE_USER_FIRST_NAME_2)
        .lastName(TEST_TRAINEE_USER_LAST_NAME_2)
        .username(TEST_TRAINEE_USER_USERNAME_2)
        .password(TEST_TRAINEE_USER_PASSWORD_2)
        .isActive(TEST_TRAINEE_USER_IS_ACTIVE_2)
        .build();
  }

  public static User getTraineeUser3() {
    return User.builder()
        .id(TEST_TRAINEE_USER_ID_3)
        .firstName(TEST_TRAINEE_USER_FIRST_NAME_3)
        .lastName(TEST_TRAINEE_USER_LAST_NAME_3)
        .username(TEST_TRAINEE_USER_USERNAME_3)
        .password(TEST_TRAINEE_USER_PASSWORD_3)
        .isActive(TEST_TRAINEE_USER_IS_ACTIVE_3)
        .build();
  }

  public static User getTraineeUser4() {
    return User.builder()
        .id(TEST_TRAINEE_USER_ID_4)
        .firstName(TEST_TRAINEE_USER_FIRST_NAME_4)
        .lastName(TEST_TRAINEE_USER_LAST_NAME_4)
        .username(TEST_TRAINEE_USER_USERNAME_4)
        .password(TEST_TRAINEE_USER_PASSWORD_4)
        .isActive(TEST_TRAINEE_USER_IS_ACTIVE_4)
        .build();
  }

  public static User getTrainerUser1() {
    return User.builder()
        .id(TEST_TRAINER_USER_ID_1)
        .firstName(TEST_TRAINER_USER_FIRST_NAME_1)
        .lastName(TEST_TRAINER_USER_LAST_NAME_1)
        .username(TEST_TRAINER_USER_USERNAME_1)
        .password(TEST_TRAINER_USER_PASSWORD_1)
        .isActive(TEST_TRAINER_USER_IS_ACTIVE_1)
        .build();
  }

  public static User getTrainerUser2() {
    return User.builder()
        .id(TEST_TRAINER_USER_ID_2)
        .firstName(TEST_TRAINER_USER_FIRST_NAME_2)
        .lastName(TEST_TRAINER_USER_LAST_NAME_2)
        .username(TEST_TRAINER_USER_USERNAME_2)
        .password(TEST_TRAINER_USER_PASSWORD_2)
        .isActive(TEST_TRAINER_USER_IS_ACTIVE_2)
        .build();
  }

  public static User getTrainerUser3() {
    return User.builder()
        .id(TEST_TRAINER_USER_ID_3)
        .firstName(TEST_TRAINER_USER_FIRST_NAME_3)
        .lastName(TEST_TRAINER_USER_LAST_NAME_3)
        .username(TEST_TRAINER_USER_USERNAME_3)
        .password(TEST_TRAINER_USER_PASSWORD_3)
        .isActive(TEST_TRAINER_USER_IS_ACTIVE_3)
        .build();
  }

  public static User getTrainerUser4() {
    return User.builder()
        .id(TEST_TRAINER_USER_ID_4)
        .firstName(TEST_TRAINER_USER_FIRST_NAME_4)
        .lastName(TEST_TRAINER_USER_LAST_NAME_4)
        .username(TEST_TRAINER_USER_USERNAME_4)
        .password(TEST_TRAINER_USER_PASSWORD_4)
        .isActive(TEST_TRAINER_USER_IS_ACTIVE_4)
        .build();
  }

  public static User getUser9() {
    return User.builder()
        .id(TEST_USER_ID_9)
        .firstName(TEST_USER_FIRST_NAME_9)
        .lastName(TEST_USER_LAST_NAME_9)
        .username(TEST_USER_USERNAME_9)
        .password(TEST_USER_PASSWORD_9)
        .isActive(TEST_USER_IS_ACTIVE_9)
        .build();
  }

  public static JwtDto getUserJwtDto(String token) {
    return new JwtDto(token);
  }

  public static UserCredentialsDto getTraineeUserCredentialsDto1() {
    return UserCredentialsDto.builder()
        .username(TEST_TRAINEE_USER_USERNAME_1)
        .password(TEST_TRAINEE_USER_PASSWORD_CHAR_ARRAY_1)
        .build();
  }

  public static UserCredentialsDto getTraineeUserCredentialsDto1WithToken(String token) {
    UserCredentialsDto traineeUserCredentialsDto1 = getTraineeUserCredentialsDto1();
    traineeUserCredentialsDto1.setToken(token);

    return traineeUserCredentialsDto1;
  }

  public static UserCredentialsDto getTrainerUserCredentialsDto1() {
    return UserCredentialsDto.builder()
        .username(TEST_TRAINER_USER_USERNAME_1)
        .password(TEST_TRAINER_USER_PASSWORD_CHAR_ARRAY_1)
        .build();
  }

  public static UserCredentialsDto getTrainerUserCredentialsDto1WithToken(String token) {
    UserCredentialsDto trainerUserCredentialsDto1 = getTrainerUserCredentialsDto1();
    trainerUserCredentialsDto1.setToken(token);

    return trainerUserCredentialsDto1;
  }

  public static ChangePasswordDto getChangePasswordDto1() {
    return ChangePasswordDto.builder()
        .username(TEST_TRAINEE_USER_USERNAME_1)
        .oldPassword(TEST_TRAINEE_USER_PASSWORD_CHAR_ARRAY_1)
        .newPassword(TEST_TRAINEE_USER_NEW_PASSWORD_CHAR_ARRAY_1)
        .build();
  }

  public static UserActivateDto getUserActivation1() {
    return UserActivateDto.builder()
        .username(TEST_TRAINEE_USER_USERNAME_1)
        .isActive(true)
        .build();
  }

  public static UserActivateDto getUserDeactivation1() {
    return UserActivateDto.builder()
        .username(TEST_TRAINEE_USER_USERNAME_1)
        .isActive(false)
        .build();
  }
}
