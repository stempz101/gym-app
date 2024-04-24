package com.epam.gymapp.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.GymAppApplication;
import com.epam.gymapp.config.TestHibernateConfiguration;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@ContextConfiguration(classes = {GymAppApplication.class, TestHibernateConfiguration.class})
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TrainingRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(TrainingRepositoryTest.class);

  @Autowired
  private TrainingRepository trainingRepository;

  @Test
  @Order(1)
  void save_CreateCase_Success() {
    // Given
    Training training = TrainingTestUtil.getNewTraining8();

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
  @Order(3)
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
  @Order(4)
  void findAllByTrainerUsernameAndParams() {
    // Given
    String username = "John.Doe";
    LocalDate fromDate = LocalDate.of(2024, 2, 10);
    LocalDate toDate = LocalDate.of(2024, 2, 27);
    String traineeName = "mich";
    int expectedResultSize = 1;

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
  @Order(5)
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
  @Order(6)
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
  @Order(7)
  void countOfUpcomingTrainings_Success() {
    // Given
    LocalDate currentDate = LocalDate.of(2024, 2, 10);
    long expectedResult = 3;

    // When
    long result = trainingRepository.countOfUpcomingTrainings(currentDate);
    log.debug("count_Success: result {}", result);

    // Then
    assertThat(result, greaterThanOrEqualTo(expectedResult));
  }

  @Test
  @Order(8)
  void delete_Success() {
    // Given
    Training training = TrainingTestUtil.getTraining7();

    // When
    trainingRepository.delete(training);
    Optional<Training> result = trainingRepository.findById(training.getId());

    // Then
    assertTrue(result.isEmpty());
  }
}
