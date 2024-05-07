package com.epam.gymapp.mainmicroservice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.mainmicroservice.config.TestRedisConfiguration;
import com.epam.gymapp.mainmicroservice.model.SessionUser;
import com.epam.gymapp.mainmicroservice.test.utils.JwtTokenTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.SessionUserTestUtil;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRedisConfiguration.class)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SessionUserRepositoryTest {

  @Autowired
  private SessionUserRepository sessionUserRepository;

  @Autowired
  private RedisTemplate<String, SessionUser> redisTemplate;

  @Autowired
  private JwtTokenTestUtil jwtTokenTestUtil;

  @PostConstruct
  public void init() {
    SessionUser sessionUser1 = SessionUserTestUtil.getSessionUser1();
    SessionUser sessionUser2 = SessionUserTestUtil.getSessionUser2();

    String hash1 = SessionUserTestUtil.TEST_SESSION_USER_HASH_1;
    String hash2 = SessionUserTestUtil.TEST_SESSION_USER_HASH_2;

    redisTemplate.opsForValue()
        .set(jwtTokenTestUtil.buildSessionUserId(sessionUser1.getUsername(), hash1), sessionUser1);
    redisTemplate.opsForValue()
        .set(jwtTokenTestUtil.buildSessionUserId(sessionUser2.getUsername(), hash2), sessionUser2);
  }

  @Test
  @Order(1)
  void save_CreateCase_Success() {
    // Given
    SessionUser sessionUser = SessionUserTestUtil.getSessionUser3();
    String hash = SessionUserTestUtil.TEST_SESSION_USER_HASH_3;
    String id = jwtTokenTestUtil.buildSessionUserId(sessionUser.getUsername(), hash);

    // When
    sessionUserRepository.save(id, sessionUser);

    // Then
    SessionUser result = redisTemplate.opsForValue().get(id);
    assertEquals(sessionUser, result);
  }

  @Test
  @Order(2)
  void findByUsername_Success() {
    // Given
    SessionUser sessionUser = SessionUserTestUtil.getSessionUser2();
    String hash = SessionUserTestUtil.TEST_SESSION_USER_HASH_2;
    String id = jwtTokenTestUtil.buildSessionUserId(sessionUser.getUsername(), hash);

    // When
    Optional<SessionUser> result = sessionUserRepository.findById(id);

    // Then
    assertTrue(result.isPresent());
    assertThat(result.get(), allOf(
        hasProperty("username", equalTo(sessionUser.getUsername())),
        hasProperty("createdAt", equalTo(sessionUser.getCreatedAt()))
    ));
  }

  @Test
  @Order(3)
  void deleteByUsername_Success() {
    // Given
    SessionUser sessionUser = SessionUserTestUtil.getSessionUser2();
    String hash = SessionUserTestUtil.TEST_SESSION_USER_HASH_2;
    String id = jwtTokenTestUtil.buildSessionUserId(sessionUser.getUsername(), hash);

    // When
    sessionUserRepository.deleteByUsername(sessionUser.getUsername());

    // Then
    SessionUser result = redisTemplate.opsForValue().get(id);
    assertNull(result);
  }
}
