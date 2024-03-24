package com.epam.gymapp.test.utils;

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.User;
import java.util.List;

public class TrainerTestUtil {

  public static final long TEST_TRAINER_ID_1 = 1;
  public static final List<Trainee> TEST_TRAINER_TRAINEES_1 = List.of(
      TraineeTestUtil.getTrainee3(),
      TraineeTestUtil.getTrainee2()
  );

  public static final long TEST_TRAINER_ID_2 = 2;
  public static final List<Trainee> TEST_TRAINER_TRAINEES_2 = List.of(
      TraineeTestUtil.getTrainee1(),
      TraineeTestUtil.getTrainee3()
  );

  public static final long TEST_TRAINER_ID_3 = 3;
  public static final List<Trainee> TEST_TRAINER_TRAINEES_3 = List.of(
      TraineeTestUtil.getTrainee2(),
      TraineeTestUtil.getTrainee4()
  );

  public static final long TEST_TRAINER_ID_4 = 4;

  public static Trainer getNewTrainer5() {
    return Trainer.builder()
        .user(UserTestUtil.getNewTrainerUser1())
        .specialization(TrainingTypeTestUtil.getNewTrainingType4())
        .build();
  }

  public static Trainer getNewTrainer6() {
    return Trainer.builder()
        .user(UserTestUtil.getNewTrainerUser2())
        .specialization(TrainingTypeTestUtil.getNewTrainingType1())
        .build();
  }

  public static Trainer getTrainer1() {
    return Trainer.builder()
        .id(TEST_TRAINER_ID_1)
        .user(UserTestUtil.getTrainerUser1())
        .specialization(TrainingTypeTestUtil.getTrainingType1())
        .trainees(TEST_TRAINER_TRAINEES_1)
        .build();
  }

  public static Trainer getTrainer2() {
    return Trainer.builder()
        .id(TEST_TRAINER_ID_2)
        .user(UserTestUtil.getTrainerUser2())
        .specialization(TrainingTypeTestUtil.getTrainingType2())
        .trainees(TEST_TRAINER_TRAINEES_2)
        .build();
  }

  public static Trainer getTrainer3() {
    return Trainer.builder()
        .id(TEST_TRAINER_ID_3)
        .user(UserTestUtil.getTrainerUser3())
        .specialization(TrainingTypeTestUtil.getTrainingType3())
        .trainees(TEST_TRAINER_TRAINEES_3)
        .build();
  }

  public static Trainer getTrainer4() {
    return Trainer.builder()
        .id(TEST_TRAINER_ID_4)
        .user(UserTestUtil.getTrainerUser4())
        .specialization(TrainingTypeTestUtil.getTrainingType1())
        .build();
  }

  public static List<Trainer> getTrainers() {
    return List.of(getTrainer1(), getTrainer2(), getTrainer3());
  }

  public static TrainerCreateDto getTrainerCreateDto1() {
    return TrainerCreateDto.builder()
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1)
        .specialization(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_1)
        .build();
  }

  public static TrainerUpdateDto getTrainerUpdateDto1() {
    return TrainerUpdateDto.builder()
        .username(UserTestUtil.TEST_TRAINER_USER_USERNAME_1)
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1)
        .specialization(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_1)
        .isActive(false)
        .build();
  }

  public static Trainer getTrainer1FromTrainerCreateDto() {
    User trainerUser = UserTestUtil.getTrainerUser1();
    trainerUser.setId(null);

    return Trainer.builder()
        .user(trainerUser)
        .specialization(TrainingTypeTestUtil.getNewTrainingType1())
        .build();
  }

  public static TrainerInfoDto getTrainerInfoDto1() {
    return TrainerInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1)
        .specialization(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_1)
        .trainees(List.of(
            TraineeTestUtil.getTraineeShortInfoDto3(),
            TraineeTestUtil.getTraineeShortInfoDto2()
        ))
        .build();
  }

  public static TrainerInfoDto getTrainerInfoDto1AfterUpdate() {
    return TrainerInfoDto.builder()
        .username(UserTestUtil.TEST_TRAINER_USER_USERNAME_1)
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1)
        .specialization(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_1)
        .trainees(List.of(
            TraineeTestUtil.getTraineeShortInfoDto3(),
            TraineeTestUtil.getTraineeShortInfoDto2()
        ))
        .build();
  }

  public static TrainerInfoDto getTrainerInfoDto2() {
    return TrainerInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_2)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_2)
        .specialization(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_2)
        .trainees(List.of(
            TraineeTestUtil.getTraineeShortInfoDto1(),
            TraineeTestUtil.getTraineeShortInfoDto3()
        ))
        .build();
  }

  public static TrainerInfoDto getTrainerInfoDto3() {
    return TrainerInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3)
        .specialization(TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_3)
        .trainees(List.of(
            TraineeTestUtil.getTraineeShortInfoDto2(),
            TraineeTestUtil.getTraineeShortInfoDto4()
        ))
        .build();
  }

  public static List<TrainerInfoDto> getTrainerInfoDtos() {
    return List.of(getTrainerInfoDto1(), getTrainerInfoDto2(), getTrainerInfoDto3());
  }

  public static TrainerShortInfoDto getTrainerShortInfoDto1() {
    return TrainerShortInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1)
        .username(UserTestUtil.TEST_TRAINER_USER_USERNAME_1)
        .specialization(TrainingTypeTestUtil.getTrainingType1().getName())
        .build();
  }

  public static TrainerShortInfoDto getTrainerShortInfoDto2() {
    return TrainerShortInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_2)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_2)
        .username(UserTestUtil.TEST_TRAINER_USER_USERNAME_2)
        .specialization(TrainingTypeTestUtil.getTrainingType2().getName())
        .build();
  }

  public static TrainerShortInfoDto getTrainerShortInfoDto3() {
    return TrainerShortInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3)
        .username(UserTestUtil.TEST_TRAINER_USER_USERNAME_3)
        .specialization(TrainingTypeTestUtil.getTrainingType3().getName())
        .build();
  }

  public static TrainerShortInfoDto getTrainerShortInfoDto4() {
    return TrainerShortInfoDto.builder()
        .firstName(UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_4)
        .lastName(UserTestUtil.TEST_TRAINER_USER_LAST_NAME_4)
        .username(UserTestUtil.TEST_TRAINER_USER_USERNAME_4)
        .specialization(TrainingTypeTestUtil.getTrainingType1().getName())
        .build();
  }

  public static List<Trainer> getUnassignedTrainersOnTrainee1() {
    return List.of(getTrainer1(), getTrainer3(), getTrainer4());
  }

  public static List<TrainerShortInfoDto> getUnassignedTrainerShortInfoDtosOnTrainee1() {
    return List.of(getTrainerShortInfoDto1(), getTrainerShortInfoDto3(), getTrainerShortInfoDto4());
  }
}
