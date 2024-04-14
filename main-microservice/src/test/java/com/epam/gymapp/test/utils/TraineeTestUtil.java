package com.epam.gymapp.test.utils;

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeShortInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.User;
import java.time.LocalDate;
import java.util.List;

public class TraineeTestUtil {

  public static final long TEST_TRAINEE_ID_1 = 1;
  public static final LocalDate TEST_TRAINEE_DATE_OF_BIRTH_1 = LocalDate.of(2001, 7, 9);
  public static final String TEST_TRAINEE_ADDRESS_1 = "Deribasovska St, 1, Odesa";
  public static final List<Trainer> TEST_TRAINEE_TRAINERS_1 = List.of(
      TrainerTestUtil.getTrainer2()
  );

  public static final long TEST_TRAINEE_ID_2 = 2;
  public static final LocalDate TEST_TRAINEE_DATE_OF_BIRTH_2 = LocalDate.of(2000, 3, 20);
  public static final String TEST_TRAINEE_ADDRESS_2 = "Hrets'ka St, 56, Odesa";
  public static final List<Trainer> TEST_TRAINEE_TRAINERS_2 = List.of(
      TrainerTestUtil.getTrainer3(),
      TrainerTestUtil.getTrainer1()
  );

  public static final long TEST_TRAINEE_ID_3 = 3;
  public static final LocalDate TEST_TRAINEE_DATE_OF_BIRTH_3 = LocalDate.of(1999, 9, 5);
  public static final String TEST_TRAINEE_ADDRESS_3 = "Kanatna St, 4, Odesa";
  public static final List<Trainer> TEST_TRAINEE_TRAINERS_3 = List.of(
      TrainerTestUtil.getTrainer1(),
      TrainerTestUtil.getTrainer2()
  );

  public static final long TEST_TRAINEE_ID_4 = 4;
  public static final LocalDate TEST_TRAINEE_DATE_OF_BIRTH_4 = LocalDate.of(1996, 10, 8);
  public static final String TEST_TRAINEE_ADDRESS_4 = "Preobrazhens'ka St, 33, Odesa";
  public static final List<Trainer> TEST_TRAINEE_TRAINERS_4 = List.of(
      TrainerTestUtil.getTrainer3()
  );

  public static final LocalDate TEST_NEW_TRAINEE_DATE_OF_BIRTH = LocalDate.of(1997, 10, 23);
  public static final String TEST_NEW_TRAINEE_ADDRESS = "Preobrazhens'ka St, 1, Odesa";

  public static Trainee getNewTrainee5() {
    return Trainee.builder()
        .user(UserTestUtil.getNewTraineeUser1())
        .dateOfBirth(TEST_NEW_TRAINEE_DATE_OF_BIRTH)
        .address(TEST_NEW_TRAINEE_ADDRESS)
        .build();
  }

  public static Trainee getTrainee1() {
    return Trainee.builder()
        .id(TEST_TRAINEE_ID_1)
        .user(UserTestUtil.getTraineeUser1())
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .trainers(TEST_TRAINEE_TRAINERS_1)
        .build();
  }

  public static Trainee getTrainee2() {
    return Trainee.builder()
        .id(TEST_TRAINEE_ID_2)
        .user(UserTestUtil.getTraineeUser2())
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_2)
        .address(TEST_TRAINEE_ADDRESS_2)
        .trainers(TEST_TRAINEE_TRAINERS_2)
        .build();
  }

  public static Trainee getTrainee3() {
    return Trainee.builder()
        .id(TEST_TRAINEE_ID_3)
        .user(UserTestUtil.getTraineeUser3())
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_3)
        .address(TEST_TRAINEE_ADDRESS_3)
        .trainers(TEST_TRAINEE_TRAINERS_3)
        .build();
  }

  public static Trainee getTrainee4() {
    return Trainee.builder()
        .id(TEST_TRAINEE_ID_4)
        .user(UserTestUtil.getTraineeUser4())
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_4)
        .address(TEST_TRAINEE_ADDRESS_4)
        .trainers(TEST_TRAINEE_TRAINERS_4)
        .build();
  }

  public static List<Trainee> getTrainees() {
    return List.of(getTrainee1(), getTrainee2(), getTrainee3());
  }

  public static TraineeCreateDto getTraineeCreateDto1() {
    return TraineeCreateDto.builder()
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .build();
  }

  public static TraineeUpdateDto getTraineeUpdateDto1() {
    return TraineeUpdateDto.builder()
        .username(UserTestUtil.TEST_TRAINER_USER_USERNAME_1)
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .isActive(false)
        .build();
  }

  public static Trainee getTrainee1FromTraineeCreateDto() {
    User traineeUser = UserTestUtil.getTraineeUser1();
    traineeUser.setId(null);

    return Trainee.builder()
        .user(traineeUser)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .build();
  }

  public static TraineeInfoDto getTraineeInfoDto1() {
    return TraineeInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .trainers(List.of(
            TrainerTestUtil.getTrainerShortInfoDto2()
        ))
        .build();
  }

  public static TraineeInfoDto getTraineeInfoDto1AfterUpdate() {
    return TraineeInfoDto.builder()
        .username(UserTestUtil.TEST_TRAINEE_USER_USERNAME_1)
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_1)
        .address(TEST_TRAINEE_ADDRESS_1)
        .trainers(List.of(
            TrainerTestUtil.getTrainerShortInfoDto2()
        ))
        .build();
  }

  public static TraineeInfoDto getTraineeInfoDto2() {
    return TraineeInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_2)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_2)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_2)
        .address(TEST_TRAINEE_ADDRESS_2)
        .trainers(List.of(
            TrainerTestUtil.getTrainerShortInfoDto3(),
            TrainerTestUtil.getTrainerShortInfoDto1()
        ))
        .build();
  }

  public static TraineeInfoDto getTraineeInfoDto3() {
    return TraineeInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_3)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_3)
        .dateOfBirth(TEST_TRAINEE_DATE_OF_BIRTH_3)
        .address(TEST_TRAINEE_ADDRESS_3)
        .trainers(List.of(
            TrainerTestUtil.getTrainerShortInfoDto1(),
            TrainerTestUtil.getTrainerShortInfoDto2()
        ))
        .build();
  }

  public static List<TraineeInfoDto> getTraineeInfoDtos() {
    return List.of(getTraineeInfoDto1(), getTraineeInfoDto2(), getTraineeInfoDto3());
  }

  public static TraineeShortInfoDto getTraineeShortInfoDto1() {
    return TraineeShortInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1)
        .username(UserTestUtil.TEST_TRAINEE_USER_USERNAME_1)
        .build();
  }

  public static TraineeShortInfoDto getTraineeShortInfoDto2() {
    return TraineeShortInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_2)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_2)
        .username(UserTestUtil.TEST_TRAINEE_USER_USERNAME_2)
        .build();
  }

  public static TraineeShortInfoDto getTraineeShortInfoDto3() {
    return TraineeShortInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_3)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_3)
        .username(UserTestUtil.TEST_TRAINEE_USER_USERNAME_3)
        .build();
  }

  public static TraineeShortInfoDto getTraineeShortInfoDto4() {
    return TraineeShortInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_4)
        .lastName(UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_4)
        .username(UserTestUtil.TEST_TRAINEE_USER_USERNAME_4)
        .build();
  }

  public static TraineeTrainersUpdateDto getTraineeTrainersUpdateDto2() {
    return TraineeTrainersUpdateDto.builder()
        .traineeUsername(UserTestUtil.TEST_TRAINEE_USER_USERNAME_2)
        .trainerUsernames(List.of(
            UserTestUtil.TEST_TRAINER_USER_USERNAME_1,
            UserTestUtil.TEST_TRAINER_USER_USERNAME_2
        ))
        .build();
  }

  public static List<Trainer> getTraineeUpdatedTrainersList2() {
    return List.of(
        TrainerTestUtil.getTrainer1(),
        TrainerTestUtil.getTrainer2()
    );
  }

  public static List<TrainerShortInfoDto> getTraineeUpdatedTrainerShortInfoDtosList2() {
    return List.of(
        TrainerTestUtil.getTrainerShortInfoDto1(),
        TrainerTestUtil.getTrainerShortInfoDto2()
    );
  }
}
