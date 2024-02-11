package com.epam.gymapp;

import com.epam.gymapp.config.GymApplicationConfiguration;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.facade.GymFacade;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.model.TrainingType;
import java.time.Duration;
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

		TraineeCreateDto traineeCreateDto = TraineeCreateDto.builder()
				.firstName("Christopher")
				.lastName("Lee")
				.dateOfBirth(LocalDate.of(1998, 10,5))
				.address("Shevchenka av, 1, Odesa")
				.build();
		TrainerCreateDto trainerCreateDto = TrainerCreateDto.builder()
				.firstName("John")
				.lastName("Doe")
				.specialization(new TrainingType("Yoga"))
				.build();
		TrainingCreateDto trainingCreateDto = TrainingCreateDto.builder()
				.name("Training #111")
				.date(LocalDate.of(2024, 2, 15))
				.duration(Duration.ofHours(1))
				.build();

		Trainee traineeUpdate = Trainee.builder()
				.firstName(traineeCreateDto.getFirstName())
				.lastName("Johnson")
				.username("my_trainee_username")
				.password("my_trainee_password".toCharArray())
				.dateOfBirth(traineeCreateDto.getDateOfBirth())
				.address(traineeCreateDto.getAddress())
				.build();
		Trainer trainerUpdate = Trainer.builder()
				.firstName("Max")
				.lastName(trainerCreateDto.getLastName())
				.username("my_trainer_username")
				.password("my_trainer_password".toCharArray())
				.specialization(new TrainingType("Functional training"))
				.build();

		List<Trainee> traineesBefore = gymFacade.selectAllTrainees();
		List<Trainer> trainersBefore = gymFacade.selectAllTrainers();
		List<Training> trainingsBefore = gymFacade.selectAllTrainings();
		log.info("All Trainees before the next actions: {}", traineesBefore);
		log.info("All Trainers before the next actions: {}", trainersBefore);
		log.info("All Trainings before the next actions: {}", trainingsBefore);

		Trainee trainee1 = gymFacade.createTraineeProfile(traineeCreateDto);

		traineeUpdate.setUserId(trainee1.getUserId());
		trainee1 = gymFacade.updateTraineeProfile(traineeUpdate);
		log.info("Updated Trainee: {}", trainee1);
		log.info("Updated Trainee's username: {}", trainee1.getUsername());
		log.info("Updated Trainee's password: {}", trainee1.getPassword());

		trainee1 = gymFacade.selectTraineeProfile(trainee1.getUserId());
		log.info("Selected Trainee with ID={}: {}", trainee1.getUserId(), trainee1);

		Trainer trainer = gymFacade.createTrainerProfile(trainerCreateDto);
		log.info("Created Trainer: {}", trainer);
		log.info("Trainer's username: {}", trainer.getUsername());
		log.info("Trainer's password: {}", trainer.getPassword());

		trainerUpdate.setUserId(trainer.getUserId());
		trainer = gymFacade.updateTrainerProfile(trainerUpdate);
		log.info("Updated Trainer: {}", trainer);
		log.info("Updated Trainer's username: {}", trainer.getUsername());
		log.info("Updated Trainer's password: {}", trainer.getPassword());

		trainer = gymFacade.selectTrainerProfile(trainer.getUserId());
		log.info("Selected Trainer with ID={}: {}", trainer.getUserId(), trainer);

		trainingCreateDto.setTraineeId(trainee1.getUserId());
		trainingCreateDto.setTrainerId(trainer.getUserId());
		trainingCreateDto.setType(trainer.getSpecialization());

		Training training = gymFacade.createTraining(trainingCreateDto);
		log.info("Created Training: {}", training);

		training = gymFacade.selectTraining(training.getId());
		log.info("Selected Training with ID={}: {}", training.getId(), training);

		log.info("Deleting Trainee with ID: {}", 2L);
		gymFacade.deleteTraineeProfile(2L);
		log.info("Deleting Trainee with ID: {}", 3L);
		gymFacade.deleteTraineeProfile(3L);

		List<Trainee> traineesAfter = gymFacade.selectAllTrainees();
		List<Trainer> trainersAfter = gymFacade.selectAllTrainers();
		List<Training> trainingsAfter = gymFacade.selectAllTrainings();
		log.info("All Trainees after the next actions: {}", traineesAfter);
		log.info("All Trainers after the next actions: {}", trainersAfter);
		log.info("All Trainings after the next actions: {}", trainingsAfter);
	}
}
