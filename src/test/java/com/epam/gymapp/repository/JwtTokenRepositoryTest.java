package com.epam.gymapp.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.GymAppApplication;
import com.epam.gymapp.config.TestHibernateConfiguration;
import com.epam.gymapp.model.JwtToken;
import com.epam.gymapp.model.User;
import com.epam.gymapp.test.utils.JwtTokenTestUtil;
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
public class JwtTokenRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(JwtTokenRepositoryTest.class);

  @Autowired
  private JwtTokenRepository jwtTokenRepository;

  @Test
  @Order(1)
  void save_CreateCase_Success() {
    // Given
    JwtToken jwt = JwtTokenTestUtil.getNewJwtTokenOfTrainee1();

    // When
    JwtToken result = jwtTokenRepository.save(jwt);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        hasProperty("id", greaterThan(3)),
        hasProperty("token", equalTo(jwt.getToken())),
        hasProperty("revoked", equalTo(jwt.isRevoked())),
        hasProperty("user", equalTo(jwt.getUser()))
    ));
  }

  @Test
  @Order(2)
  void findAllValidTokensByUserId_Success() {
    // Given
    long userId = UserTestUtil.TEST_TRAINEE_USER_ID_1;
    List<JwtToken> jwtList = JwtTokenTestUtil.getValidJwtTokensOfTrainee1();

    // When
    List<JwtToken> result = jwtTokenRepository.findAllValidTokensByUserId(userId);

    // Then
    assertThat(result, allOf(
        notNullValue(),
        containsInAnyOrder(jwtList.get(0), jwtList.get(1))
    ));
  }

  @Test
  @Order(3)
  void findByToken_Success() {
    // Given
    String token = JwtTokenTestUtil.TEST_JWT_TOKEN_1_USER_1;
    JwtToken expectedResult = JwtTokenTestUtil.getJwtToken1OfTrainee1();

    // When
    Optional<JwtToken> result = jwtTokenRepository.findByToken(token);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get(), samePropertyValuesAs(expectedResult));
  }
}
