package com.epam.gymapp.mainmicroservice.test.utils;

import com.epam.gymapp.mainmicroservice.model.SessionUser;
import java.time.LocalDateTime;

public class SessionUserTestUtil {

  public static final String TEST_SESSION_USER_HASH_1 = "hash1";
  public static final LocalDateTime TEST_SESSION_USER_CREATED_AT_1 =
      LocalDateTime.of(2024, 3, 10, 13, 10);

  public static final String TEST_SESSION_USER_HASH_2 = "hash2";
  public static final LocalDateTime TEST_SESSION_USER_CREATED_AT_2 =
      LocalDateTime.of(2024, 3, 23, 5, 32);

  public static final String TEST_SESSION_USER_HASH_3 = "hash3";
  public static final LocalDateTime TEST_SESSION_USER_CREATED_AT_3 =
      LocalDateTime.of(2024, 4, 14, 11, 56);

  public static SessionUser getSessionUser1() {
    return SessionUser.builder()
        .username(UserTestUtil.TEST_TRAINEE_USER_USERNAME_1)
        .createdAt(TEST_SESSION_USER_CREATED_AT_1)
        .build();
  }

  public static SessionUser getSessionUser2() {
    return SessionUser.builder()
        .username(UserTestUtil.TEST_TRAINEE_USER_USERNAME_2)
        .createdAt(TEST_SESSION_USER_CREATED_AT_2)
        .build();
  }

  public static SessionUser getSessionUser3() {
    return SessionUser.builder()
        .username(UserTestUtil.TEST_TRAINEE_USER_USERNAME_3)
        .createdAt(TEST_SESSION_USER_CREATED_AT_3)
        .build();
  }
}
