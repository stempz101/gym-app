package com.epam.gymapp.mainmicroservice.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.mainmicroservice.model.User;
import com.epam.gymapp.mainmicroservice.test.utils.UserTestUtil;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UserUtilsTest {

  @Test
  void getNumberOfAppearances_ListIsNotEmpty_Success() {
    // Given
    List<User> users = List.of(
        User.builder().username("Will.Smith").build(),
        User.builder().username("Will.Smith5").build(),
        User.builder().username("Will.Smith3").build()
    );
    int expectedResult = 6;

    // When
    int result = UserUtils.getNumberOfAppearances(users);

    // Then
    assertEquals(expectedResult, result);
  }

  @Test
  void getNumberOfAppearances_ListWithOneElement_Success() {
    // Given
    List<User> users = List.of(
        User.builder().username("Will.Smith").build()
    );
    int expectedResult = 1;

    // When
    int result = UserUtils.getNumberOfAppearances(users);

    // Then
    assertEquals(expectedResult, result);
  }

  @Test
  void getNumberOfAppearances_ListIsNull_Success() {
    // Given
    int expectedResult = 0;

    // When
    int result = UserUtils.getNumberOfAppearances(null);

    // Then
    assertEquals(expectedResult, result);
  }

  @Test
  void getNumberOfAppearances_ListIsEmpty_Success() {
    // Given
    int expectedResult = 0;

    // When
    int result = UserUtils.getNumberOfAppearances(Collections.emptyList());

    // Then
    assertEquals(expectedResult, result);
  }

  @ParameterizedTest
  @MethodSource("argumentsForBuildUsername")
  void buildUsername_Success(User user, int numOfAppearance, String expectedResult) {
    // When
    String result = UserUtils.buildUsername(user, numOfAppearance);

    // Then
    assertEquals(expectedResult, result);
  }

  @Test
  void generatePassword_ExpectedLength_Success() {
    // Given
    int expected = 10;

    // When
    char[] result = UserUtils.generatePassword();

    // Then
    assertEquals(expected, result.length);
  }

  @Test
  void generatePassword_IsAlphanumeric_Success() {
    // Given
    String expected = "[A-Za-z0-9]+";

    // When
    char[] result = UserUtils.generatePassword();

    // Then
    assertTrue(String.valueOf(result).matches(expected));
  }

  @Test
  void generatePassword_IsUnique_Success() {
    // When
    char[] result1 = UserUtils.generatePassword();
    char[] result2 = UserUtils.generatePassword();

    // Then
    assertNotEquals(result1, result2);
  }

  static Stream<Arguments> argumentsForBuildUsername() {
    User user1 = UserTestUtil.getTraineeUser1();
    User user2 = UserTestUtil.getTraineeUser2();
    User user3 = UserTestUtil.getTraineeUser3();

    return Stream.of(
        Arguments.of(user1, 0, "Michael.Patel"),
        Arguments.of(user2, 3, "Michael.Patel3"),
        Arguments.of(user3, 4, "Christopher.Lee4")
    );
  }
}
