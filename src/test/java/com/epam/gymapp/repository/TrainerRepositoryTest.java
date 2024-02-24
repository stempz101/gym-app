package com.epam.gymapp.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.config.TestHibernateConfiguration;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTypeTestUtil;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestHibernateConfiguration.class})
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TrainerRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(TrainerRepositoryTest.class);

  @Autowired
  private TrainerRepository trainerRepository;

  @Test
  @Order(1)
  void save_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getNewTrainer5();

    // When
    Trainer result = trainerRepository.save(trainer);
    log.debug("save_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", greaterThan(3L)),
        hasProperty("user", allOf(
            hasProperty("id", greaterThan(7L)),
            hasProperty("firstName", equalTo(trainer.getUser().getFirstName())),
            hasProperty("lastName", equalTo(trainer.getUser().getLastName())),
            hasProperty("username", equalTo(trainer.getUser().getUsername())),
            hasProperty("password", equalTo(trainer.getUser().getPassword())),
            hasProperty("active", equalTo(trainer.getUser().isActive()))
        )),
        hasProperty("specialization", allOf(
            hasProperty("id", greaterThan(3L)),
            hasProperty("name", equalTo(trainer.getSpecialization().getName()))
        ))
    ));
  }

  @Test
  @Order(2)
  void save_GivenExistedTrainingType_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getNewTrainer6();


    // When
    Trainer result = trainerRepository.save(trainer);
    log.debug("save_GivenExistedTrainingType_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", greaterThan(3L)),
        hasProperty("user", allOf(
            hasProperty("id", greaterThan(8L)),
            hasProperty("firstName", equalTo(trainer.getUser().getFirstName())),
            hasProperty("lastName", equalTo(trainer.getUser().getLastName())),
            hasProperty("username", equalTo(trainer.getUser().getUsername())),
            hasProperty("password", equalTo(trainer.getUser().getPassword())),
            hasProperty("active", equalTo(trainer.getUser().isActive()))
        )),
        hasProperty("specialization", allOf(
            hasProperty("id", equalTo(TrainingTypeTestUtil.TEST_TRAINING_TYPE_ID_1)),
            hasProperty("name", equalTo(trainer.getSpecialization().getName()))
        ))
    ));
  }

  @Test
  @Order(3)
  void save_IfExceptionReturnNull_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getNewTrainer5();
    trainer.setUser(null);

    // When
    Trainer result = trainerRepository.save(trainer);
    log.debug("save_IfExceptionReturnNull_Success: result {}", result);

    // Then
    assertNull(result);
  }

  @Test
  @Order(4)
  void findAll_Success() {
    // When
    List<Trainer> result = trainerRepository.findAll();
    log.debug("findAll_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasSize(greaterThanOrEqualTo(3))
    ));
  }

  @Test
  @Order(5)
  void findAllUnassignedByTraineeUsername_Success() {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_3;

    // When
    List<Trainer> result = trainerRepository.findAllUnassignedByTraineeUsername(traineeUsername);
    log.debug("findAllUnassignedByTraineeUsername_Success: result {}", result);

    assertNotNull(result);
    assertThat(result, hasSize(greaterThanOrEqualTo(1)));
  }

  @Test
  @Order(6)
  void findAllByUsernameIn_Success() {
    // Given
    List<String> trainerUsernames = List.of(UserTestUtil.TEST_TRAINER_USER_USERNAME_1,
        UserTestUtil.TEST_TRAINER_USER_USERNAME_2);

    // When
    List<Trainer> result = trainerRepository.findAllByUsernameIn(trainerUsernames);
    log.debug("findAllByUsernameIn_Success: result {}", result);

    // Then
    assertNotNull(result);
    assertThat(result, hasSize(2));
    assertThat(result, hasItems(
        hasProperty("user", hasProperty("username",
            equalTo(UserTestUtil.TEST_TRAINER_USER_USERNAME_1))),
        hasProperty("user", hasProperty("username",
            equalTo(UserTestUtil.TEST_TRAINER_USER_USERNAME_2)))
    ));
  }

  @Test
  @Order(7)
  void findById_IfExistsReturnEntity_Success() {
    // Given
    long trainerId = TrainerTestUtil.TEST_TRAINER_ID_3;

    // When
    Optional<Trainer> result = trainerRepository.findById(trainerId);
    log.debug("findById_IfExistsReturnEntity_Success: result {}", result);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get().getId(), equalTo(trainerId));
  }

  @Test
  @Order(8)
  void findById_IfAbsentReturnNull_Success() {
    // Given
    long trainerId = 10;

    // When
    Optional<Trainer> result = trainerRepository.findById(trainerId);
    log.debug("findById_IfAbsentReturnNull_Success: result {}", result);

    // Then
    assertTrue(result.isEmpty());
  }

  @Test
  @Order(9)
  void findByUsername_IfExistsReturnEntity_Success() {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;

    // When
    Optional<Trainer> result = trainerRepository.findByUsername(trainerUsername);
    log.debug("findByUsername_IfExistsReturnEntity_Success: result {}", result);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get().getUser().getUsername(), equalTo(trainerUsername));
  }

  @Test
  @Order(10)
  void findByUsername_IfAbsentReturnNull_Success() {
    // Given
    String trainerUsername = "username";

    // When
    Optional<Trainer> result = trainerRepository.findByUsername(trainerUsername);
    log.debug("findByUsername_IfAbsentReturnNull_Success: result {}", result);

    // Then
    assertTrue(result.isEmpty());
  }

  @Test
  @Order(11)
  void update_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getNewTrainer6();
    trainer.setId(TrainerTestUtil.TEST_TRAINER_ID_3);
    trainer.getUser().setId(UserTestUtil.TEST_TRAINER_USER_ID_3);
    trainer.getUser().setUsername(UserTestUtil.TEST_TRAINER_USER_USERNAME_3);
    trainer.getSpecialization().setId(TrainingTypeTestUtil.TEST_TRAINING_TYPE_ID_1);

    // When
    Trainer result = trainerRepository.update(trainer);
    log.debug("update_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", equalTo(TrainerTestUtil.TEST_TRAINER_ID_3)),
        hasProperty("user", allOf(
            hasProperty("id", equalTo(UserTestUtil.TEST_TRAINER_USER_ID_3)),
            hasProperty("firstName", equalTo(trainer.getUser().getFirstName())),
            hasProperty("lastName", equalTo(trainer.getUser().getLastName())),
            hasProperty("username", equalTo(trainer.getUser().getUsername())),
            hasProperty("password", equalTo(trainer.getUser().getPassword())),
            hasProperty("active", equalTo(trainer.getUser().isActive()))
        )),
        hasProperty("specialization", allOf(
            hasProperty("id", equalTo(trainer.getSpecialization().getId())),
            hasProperty("name", equalTo(trainer.getSpecialization().getName()))
        ))
    ));
  }

  @Test
  @Order(12)
  void update_IfExceptionReturnNull_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getNewTrainer6();
    trainer.setUser(null);

    // When
    Trainer result = trainerRepository.update(trainer);
    log.debug("update_IfExceptionReturnNull_Success: result {}", result);

    // Then
    assertNull(result);
  }

  @Test
  @Order(13)
  void delete_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer1();

    // When
    trainerRepository.delete(trainer);
  }

  @Test
  @Order(14)
  void delete_IfException_Success() {
    // Given
    Trainer trainer = TrainerTestUtil.getTrainer4();
    trainer.setId(-1L);
    trainer.setUser(null);

    // When
    trainerRepository.delete(trainer);
  }
}
