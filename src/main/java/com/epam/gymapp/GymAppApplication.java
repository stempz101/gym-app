package com.epam.gymapp;

import com.epam.gymapp.config.GymApplicationConfiguration;
import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.facade.GymFacade;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GymAppApplication {

  private static final Logger log = LoggerFactory.getLogger(GymAppApplication.class);

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(GymApplicationConfiguration.class);

    GymFacade gymFacade = context.getBean(GymFacade.class);

    TraineeCreateDto traineeCreateDto1 = TraineeCreateDto.builder()
        .firstName("Christopher")
        .lastName("Lee")
        .dateOfBirth(LocalDate.of(2001, 10, 23))
        .address("West str, New York")
        .build();
    TraineeCreateDto traineeCreateDto2 = TraineeCreateDto.builder()
        .firstName("Christopher")
        .lastName("Lee")
        .dateOfBirth(LocalDate.of(1998, 5, 11))
        .address("North str, New York")
        .build();
    TraineeCreateDto traineeCreateDto3 = TraineeCreateDto.builder()
        .firstName("Alan")
        .lastName("Johnson")
        .dateOfBirth(LocalDate.of(1999, 2, 2))
        .address("East str, New York")
        .build();

    TrainerCreateDto trainerCreateDto1 = TrainerCreateDto.builder()
        .firstName("John")
        .lastName("Doe")
        .specialization("Cardio")
        .build();
    TrainerCreateDto trainerCreateDto2 = TrainerCreateDto.builder()
        .firstName("Sam")
        .lastName("Wilson")
        .specialization("Cardio")
        .build();
    TrainerCreateDto trainerCreateDto3 = TrainerCreateDto.builder()
        .firstName("John")
        .lastName("Doe")
        .specialization("Strength")
        .build();
    TrainerCreateDto trainerCreateDto4 = TrainerCreateDto.builder()
        .firstName("Mads")
        .lastName("Kristensen")
        .specialization("Yoga")
        .build();

    UserCredentialsDto traineeCreds1 = gymFacade.createTraineeProfile(traineeCreateDto1);
    UserCredentialsDto traineeCreds2 = gymFacade.createTraineeProfile(traineeCreateDto2);
    UserCredentialsDto traineeCreds3 = gymFacade.createTraineeProfile(traineeCreateDto3);

    UserCredentialsDto trainerCreds1 = gymFacade.createTrainerProfile(trainerCreateDto1);
    UserCredentialsDto trainerCreds2 = gymFacade.createTrainerProfile(trainerCreateDto2);
    UserCredentialsDto trainerCreds3 = gymFacade.createTrainerProfile(trainerCreateDto3);
    UserCredentialsDto trainerCreds4 = gymFacade.createTrainerProfile(trainerCreateDto4);

    List<TraineeInfoDto> trainees = gymFacade.selectAllTrainees(traineeCreds1);
    log.info("Created Trainees: {}", trainees);

    List<TrainerInfoDto> trainers = gymFacade.selectAllTrainers(trainerCreds1);
    log.info("Created Trainers: {}", trainers);

    TraineeInfoDto traineeInfoDto1 = gymFacade.selectTraineeProfile(
        traineeCreds2.getUsername(), traineeCreds1);
    log.info("Selected Trainee (username={}): {}", traineeCreds2.getUsername(), traineeInfoDto1);
    TraineeInfoDto traineeInfoDto3 = gymFacade.selectTraineeProfile(
        traineeCreds3.getUsername(), traineeCreds1);
    log.info("Selected Trainee (username={}): {}", traineeCreds3.getUsername(), traineeInfoDto3);

    TrainerInfoDto trainerInfoDto3 = gymFacade.selectTrainerProfile(
        trainerCreds3.getUsername(), trainerCreds2);
    log.info("Selected Trainer (username={}): {}", trainerCreds3.getUsername(), trainerInfoDto3);
    TrainerInfoDto trainerInfoDto4 = gymFacade.selectTrainerProfile(
        trainerCreds4.getUsername(), trainerCreds2);
    log.info("Selected Trainer (username={}): {}", trainerCreds4.getUsername(), trainerInfoDto4);

    char[] newPassword = "new_password".toCharArray();
    ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
        .username(traineeCreds3.getUsername())
        .oldPassword(traineeCreds3.getPassword())
        .newPassword(newPassword)
        .build();
    traineeCreds3.setPassword(newPassword);
    gymFacade.changePassword(changePasswordDto);
    log.info("Changed password for Trainee with username: {}", traineeCreds3.getUsername());

    TraineeUpdateDto traineeUpdateDto = TraineeUpdateDto.builder()
        .username(traineeCreds3.getUsername())
        .firstName(traineeInfoDto3.getFirstName())
        .lastName(traineeInfoDto3.getLastName())
        .dateOfBirth(LocalDate.of(2001, 10, 11))
        .address("Rain st, New York")
        .build();
    traineeInfoDto3 = gymFacade.updateTraineeProfile(
        traineeUpdateDto, traineeCreds1);
    log.info("Updated Trainee (username={}): {}", traineeCreds3.getUsername(), traineeInfoDto3);

    TrainerUpdateDto trainerUpdateDto = TrainerUpdateDto.builder()
        .username(trainerCreds4.getUsername())
        .firstName(trainerInfoDto4.getFirstName())
        .lastName("Johansson")
        .specialization("Something")
        .build();
    trainerInfoDto4 = gymFacade.updateTrainerProfile(trainerUpdateDto, trainerCreds1);
    log.info("Updated Trainer (username={}): {}", trainerCreds4.getUsername(), trainerInfoDto4);

    UserActivateDto userActivateDto1 = UserActivateDto.builder()
        .username(trainerCreds1.getUsername())
        .isActive(true)
        .build();
    gymFacade.changeActivationStatus(userActivateDto1, traineeCreds1);
    UserActivateDto userActivateDto2 = UserActivateDto.builder()
        .username(trainerCreds2.getUsername())
        .isActive(true)
        .build();
    gymFacade.changeActivationStatus(userActivateDto2, traineeCreds1);
    userActivateDto2.setIsActive(false);
    gymFacade.changeActivationStatus(userActivateDto2, traineeCreds1);

    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTrainersUpdateDto.builder()
        .traineeUsername(traineeCreds1.getUsername())
        .trainerUsernames(List.of(
            trainerCreds2.getUsername(),
            trainerCreds4.getUsername()
        ))
        .build();
    List<TrainerShortInfoDto> trainee1Trainers = gymFacade.updateTrainerListOfTrainee(
        traineeTrainersUpdateDto, traineeCreds1);
    log.info("Updated Trainee's (username={}) Trainer list: {}",
        traineeCreds1.getUsername(), trainee1Trainers);

    List<TrainerShortInfoDto> unassignedTrainersOnTrainee1 = gymFacade.getUnassignedTrainersOnTrainee(
        traineeCreds1.getUsername(), traineeCreds1);
    log.info("Unassigned Trainers on Trainee (username={}): {}",
        traineeCreds1.getUsername(), unassignedTrainersOnTrainee1);

    TrainingCreateDto trainingCreateDto1 = TrainingCreateDto.builder()
        .traineeUsername(traineeCreds1.getUsername())
        .trainerUsername(trainerCreds2.getUsername())
        .name("Cardio training #1")
        .date(LocalDate.of(2024, 2, 15))
        .duration(60)
        .build();
    TrainingCreateDto trainingCreateDto2 = TrainingCreateDto.builder()
        .traineeUsername(traineeCreds1.getUsername())
        .trainerUsername(trainerCreds4.getUsername())
        .name("Yoga training #1")
        .date(LocalDate.of(2024, 2, 17))
        .duration(90)
        .build();
    TrainingCreateDto trainingCreateDto3 = TrainingCreateDto.builder()
        .traineeUsername(traineeCreds1.getUsername())
        .trainerUsername(trainerCreds2.getUsername())
        .name("Cardio training #2")
        .date(LocalDate.of(2024, 2, 19))
        .duration(60)
        .build();
    TrainingCreateDto trainingCreateDto4 = TrainingCreateDto.builder()
        .traineeUsername(traineeCreds1.getUsername())
        .trainerUsername(trainerCreds4.getUsername())
        .name("Yoga training #2")
        .date(LocalDate.of(2024, 2, 21))
        .duration(90)
        .build();

    gymFacade.addTraining(trainingCreateDto1, trainerCreds1);
    gymFacade.addTraining(trainingCreateDto2, trainerCreds1);
    gymFacade.addTraining(trainingCreateDto3, trainerCreds1);
    gymFacade.addTraining(trainingCreateDto4, trainerCreds1);

    List<TrainingInfoDto> trainings = gymFacade.selectAllTrainings(trainerCreds1);
    log.info("Created Trainings: {}", trainings);

    List<TrainingInfoDto> traineeTrainings = gymFacade.getTraineeTrainings(
        traineeCreds1.getUsername(),
        LocalDate.of(2024, 2, 18),
        null,
        "mads",
        trainerInfoDto4.getSpecialization(),
        trainerCreds1
    );
    log.info("Retrieved Trainee's (username={}) Trainings: {}",
        traineeCreds1.getUsername(), traineeTrainings);

    List<TrainingInfoDto> trainerTrainings = gymFacade.getTrainerTrainings(
        trainerCreds2.getUsername(),
        LocalDate.of(2024, 2, 18),
        LocalDate.of(2024, 2, 20),
        "christopher L",
        trainerCreds1
    );
    log.info("Retrieved Trainer's (username={}) Trainings: {}",
        trainerCreds2.getUsername(), trainerTrainings);

    gymFacade.deleteTraineeProfile(traineeCreds1.getUsername(), trainerCreds1);

    trainees = gymFacade.selectAllTrainees(trainerCreds1);
    trainers = gymFacade.selectAllTrainers(trainerCreds1);
    trainings = gymFacade.selectAllTrainings(trainerCreds1);

    log.info("Current list of Trainees after making actions: {}", trainees);
    log.info("Current list of Trainers after making actions: {}", trainers);
    log.info("Current list of Trainings after making actions: {}", trainings);
  }
}
