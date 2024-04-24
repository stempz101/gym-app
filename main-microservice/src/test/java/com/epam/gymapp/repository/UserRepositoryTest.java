package com.epam.gymapp.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.GymAppApplication;
import com.epam.gymapp.config.TestHibernateConfiguration;
import com.epam.gymapp.model.User;
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
public class UserRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

  @Autowired
  private UserRepository userRepository;

  @Test
  @Order(1)
  void save_CreateCase_Success() {
    // Given
    User user = UserTestUtil.getNewTraineeUser1();

    // When
    User result = userRepository.save(user);
    log.debug("save_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", greaterThan(8L)),
        hasProperty("firstName", equalTo(user.getFirstName())),
        hasProperty("lastName", equalTo(user.getLastName())),
        hasProperty("username", equalTo(user.getUsername())),
        hasProperty("password", equalTo(user.getPassword())),
        hasProperty("active", equalTo(user.isActive()))
    ));
  }

  @Test
  @Order(2)
  void save_UpdateCase_Success() {
    // Given
    User user = UserTestUtil.getNewTrainerUser2();
    user.setId(3L);

    // When
    User result = userRepository.save(user);
    log.debug("update_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", equalTo(3L)),
        hasProperty("firstName", equalTo(user.getFirstName())),
        hasProperty("lastName", equalTo(user.getLastName())),
        hasProperty("username", equalTo(user.getUsername())),
        hasProperty("password", equalTo(user.getPassword())),
        hasProperty("active", equalTo(user.isActive()))
    ));
  }

  @Test
  @Order(3)
  void findAllByFirstAndLastNames_Success() {
    // Given
    String firstName = UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1;
    String lastName = UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1;

    // When
    List<User> result = userRepository.findAllByFirstAndLastNames(firstName, lastName);
    log.debug("findAllByFirstAndLastNames_Success: result {}", result);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasSize(2)
    ));
  }

  @Test
  @Order(4)
  void findById_IfExistsReturnEntity_Success() {
    // Given
    long userId = UserTestUtil.TEST_TRAINEE_USER_ID_1;

    // When
    Optional<User> result = userRepository.findById(userId);
    log.debug("findById_IfExistsReturnEntity_Success: result {}", result);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get().getId(), equalTo(userId));
  }

  @Test
  @Order(5)
  void findById_IfAbsentReturnNull_Success() {
    // Given
    long userId = 15;

    // When
    Optional<User> result = userRepository.findById(userId);
    log.debug("findById_IfAbsentReturnNull_Success: result {}", result);

    // Then
    assertTrue(result.isEmpty());
  }

  @Test
  @Order(6)
  void findByUsernameIgnoreCase_IfExistsReturnEntity_Success() {
    // Given
    String userUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1.toLowerCase();

    // When
    Optional<User> result = userRepository.findByUsernameIgnoreCase(userUsername);
    log.debug("findByUsername_IfExistsReturnEntity_Success: result {}", result);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get().getUsername().toUpperCase(), equalTo(userUsername.toUpperCase()));
  }

  @Test
  @Order(7)
  void findByUsernameIgnoreCase_IfAbsentReturnNull_Success() {
    // Given
    String userUsername = "username";

    // When
    Optional<User> result = userRepository.findByUsernameIgnoreCase(userUsername);
    log.debug("findByUsername_IfAbsentReturnNull_Success: result {}", result);

    // Then
    assertTrue(result.isEmpty());
  }
}
