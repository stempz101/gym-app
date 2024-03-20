package com.epam.gymapp.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.config.TestHibernateConfiguration;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.TrainingTypeTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import jakarta.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestHibernateConfiguration.class})
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TrainingRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(TrainingRepositoryTest.class);

  @Autowired
  private TrainingRepository trainingRepository;

  @Autowired
  private TraineeRepository traineeRepository;

  @Autowired
  private TrainerRepository trainerRepository;

  @Test
  @Order(1)
  void save_Success() {
    // Given
    Training training = TrainingTestUtil.getNewTraining8();
    Trainee trainee = traineeRepository.findById(training.getTrainee().getId())
        .orElse(null);
    Trainer trainer = trainerRepository.findById(training.getTrainer().getId())
        .orElse(null);
    training.setTrainee(trainee);
    training.setTrainer(trainer);

    // When
    Training result = trainingRepository.save(training);
    log.debug("save_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", greaterThan(6L)),
        hasProperty("trainee", equalTo(training.getTrainee())),
        hasProperty("trainer", equalTo(training.getTrainer())),
        hasProperty("type", equalTo(training.getType())),
        hasProperty("name", equalTo(training.getName())),
        hasProperty("date", equalTo(training.getDate())),
        hasProperty("duration", equalTo(training.getDuration()))
    ));
  }

  @Test
  @Order(2)
  void save_IfException_Failure() {
    // Given
    Training training = TrainingTestUtil.getNewTraining8();
    training.setTrainee(null);
    training.setTrainer(null);

    // When & Then
    assertThrows(PersistenceException.class, () -> trainingRepository.save(training));
  }

  @Test
  @Order(3)
  void findAll_Success() {
    // When
    List<Training> result = trainingRepository.findAll();
    log.debug("findAll_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasSize(greaterThanOrEqualTo(5))
    ));
  }

  @Test
  @Order(4)
  void findAllByTraineeUsernameAndParams_Success() {
    // Given
    String username = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    LocalDate fromDate = LocalDate.of(2024, 2, 7);
    LocalDate toDate = LocalDate.of(2024, 2, 10);
    String trainerName = "jo";
    String trainingType = "Bodybuilding";
    int expectedResultSize = 1;

    // When
    List<Training> result = trainingRepository.findAllByTraineeUsernameAndParams(username, fromDate,
        toDate, trainerName, trainingType);
    log.debug("findAllByTraineeUsernameAndParams_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasSize(expectedResultSize)
    ));
  }

  @Test
  @Order(5)
  void findAllByTrainerUsernameAndParams() {
    // Given
    String username = "John.Doe";
    LocalDate fromDate = LocalDate.of(2024, 2, 10);
    LocalDate toDate = LocalDate.of(2024, 2, 27);
    String traineeName = "mich";
    int expectedResultSize = 2;

    // When
    List<Training> result = trainingRepository.findAllByTrainerUsernameAndParams(
        username, fromDate, toDate, traineeName);
    log.debug("findAllByTrainerUsernameAndParams: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasSize(expectedResultSize)
    ));
  }

  @Test
  @Order(6)
  void findById_IfExistsReturnEntity_Success() {
    // Given
    long trainingId = TrainingTestUtil.TEST_TRAINING_ID_1;

    // When
    Optional<Training> result = trainingRepository.findById(trainingId);
    log.debug("findById_IfExistsReturnEntity_Success: result {}", result);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get().getId(), equalTo(trainingId));
  }

  @Test
  @Order(7)
  void findById_IfAbsentReturnNull_Success() {
    // Given
    long trainingId = 10;

    // When
    Optional<Training> result = trainingRepository.findById(trainingId);
    log.debug("findById_IfAbsentReturnNull_Success: result {}", result);

    // Then
    assertTrue(result.isEmpty());
  }

  @Test
  @Order(8)
  void update_Success() {
    // Given
    Training training = TrainingTestUtil.getNewTraining8();
    training.setId(TrainingTestUtil.TEST_TRAINING_ID_1);
    training.setTrainee(TraineeTestUtil.getTrainee1());
    training.setTrainer(TrainerTestUtil.getTrainer2());
    training.setType(TrainingTypeTestUtil.getTrainingType2());

    // When
    Training result = trainingRepository.update(training);
    log.debug("update_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", equalTo(training.getId())),
        hasProperty("trainee", equalTo(training.getTrainee())),
        hasProperty("trainer", equalTo(training.getTrainer())),
        hasProperty("type", equalTo(training.getType())),
        hasProperty("name", equalTo(training.getName())),
        hasProperty("date", equalTo(training.getDate())),
        hasProperty("duration", equalTo(training.getDuration()))
    ));
  }

  @Test
  @Order(9)
  void update_IfException_Failure() {
    // Given
    Training training = TrainingTestUtil.getNewTraining8();
    training.setTrainee(null);
    training.setTrainer(null);
    training.setType(null);

    // When & Then
    assertThrows(PersistenceException.class, () -> trainingRepository.update(training));
  }

  @Test
  @Order(10)
  void delete_Success() {
    // Given
    Training training = TrainingTestUtil.getTraining7();

    // When
    trainingRepository.delete(training);
  }

  @Test
  @Order(11)
  void delete_IfException_Failure() {
    // Given
    Training training = TrainingTestUtil.getTraining7();
    training.setId(-1L);
    training.setTrainee(null);
    training.setTrainer(null);
    training.setType(null);

    // When & Then
    assertThrows(PersistenceException.class, () -> trainingRepository.delete(training));
  }
}
