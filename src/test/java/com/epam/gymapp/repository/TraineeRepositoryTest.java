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
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
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
public class TraineeRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(TraineeRepositoryTest.class);

  @Autowired
  private TraineeRepository traineeRepository;

  @Autowired
  private TrainingRepository trainingRepository;

  @Test
  @Order(1)
  void save_CreateCase_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getNewTrainee5();

    // When
    Trainee result = traineeRepository.save(trainee);
    log.debug("save_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", greaterThan(4L)),
        hasProperty("user", allOf(
            hasProperty("id", greaterThan(8L)),
            hasProperty("firstName", equalTo(trainee.getUser().getFirstName())),
            hasProperty("lastName", equalTo(trainee.getUser().getLastName())),
            hasProperty("username", equalTo(trainee.getUser().getUsername())),
            hasProperty("password", equalTo(trainee.getUser().getPassword())),
            hasProperty("active", equalTo(trainee.getUser().isActive()))
        )),
        hasProperty("dateOfBirth", equalTo(trainee.getDateOfBirth())),
        hasProperty("address", equalTo(trainee.getAddress()))
    ));
  }

  @Test
  @Order(2)
  void save_UpdateCase_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getNewTrainee5();
    trainee.setId(TraineeTestUtil.TEST_TRAINEE_ID_3);
    trainee.getUser().setId(UserTestUtil.TEST_TRAINEE_USER_ID_3);
    trainee.getUser().setUsername(UserTestUtil.TEST_TRAINEE_USER_USERNAME_3);

    // When
    Trainee result = traineeRepository.save(trainee);
    log.debug("update_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", equalTo(3L)),
        hasProperty("user", allOf(
            hasProperty("id", equalTo(3L)),
            hasProperty("firstName", equalTo(trainee.getUser().getFirstName())),
            hasProperty("lastName", equalTo(trainee.getUser().getLastName())),
            hasProperty("username", equalTo(trainee.getUser().getUsername())),
            hasProperty("password", equalTo(trainee.getUser().getPassword())),
            hasProperty("active", equalTo(trainee.getUser().isActive()))
        )),
        hasProperty("dateOfBirth", equalTo(trainee.getDateOfBirth())),
        hasProperty("address", equalTo(trainee.getAddress()))
    ));
  }

  @Test
  @Order(3)
  void findAll_Success() {
    // When
    List<Trainee> result = traineeRepository.findAll();
    log.debug("findAll_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasSize(greaterThanOrEqualTo(4))
    ));
  }

  @Test
  @Order(4)
  void findById_IfExistsReturnEntity_Success() {
    // Given
    long traineeId = TraineeTestUtil.TEST_TRAINEE_ID_4;

    // When
    Optional<Trainee> result = traineeRepository.findById(traineeId);
    log.debug("findById_IfExistsReturnEntity_Success: result {}", result);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get().getId(), equalTo(traineeId));
  }

  @Test
  @Order(5)
  void findById_IfAbsentReturnNull_Success() {
    // Given
    long traineeId = 10;

    // When
    Optional<Trainee> result = traineeRepository.findById(traineeId);
    log.debug("findById_IfAbsentReturnNull_Success: result {}", result);

    // Then
    assertTrue(result.isEmpty());
  }

  @Test
  @Order(6)
  void findByUsername_IfExistsReturnEntity_Success() {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;

    // When
    Optional<Trainee> result = traineeRepository.findByUsername(traineeUsername);
    log.debug("findByUsername_IfExistsReturnEntity_Success: result {}", result);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get().getUser().getUsername(), equalTo(traineeUsername));
  }

  @Test
  @Order(7)
  void findByUsername_IfAbsentReturnNull_Success() {
    // Given
    String traineeUsername = "username";

    // When
    Optional<Trainee> result = traineeRepository.findByUsername(traineeUsername);
    log.debug("findByUsername_IfAbsentReturnNull_Success: result {}", result);

    // Then
    assertTrue(result.isEmpty());
  }

  @Test
  @Order(8)
  void delete_Success() {
    // Given
    Trainee trainee = TraineeTestUtil.getTrainee4();
    Training training = TrainingTestUtil.getTraining4();
    trainee.getTrainers().get(0).setTrainees(null);

    // When
    trainingRepository.delete(training);
    traineeRepository.delete(trainee);
    Optional<Trainee> result = traineeRepository.findById(trainee.getId());

    // Then
    assertTrue(result.isEmpty());
  }
}
