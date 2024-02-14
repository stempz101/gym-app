package com.epam.gymapp.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.gymapp.model.User;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class UserUtilsTest {

  @ParameterizedTest
  @CsvSource({
      "John.Doe, 1", "John.Smith1, 2",
      "Peter.Parker4, 5", "Ben.Johnson10, 11"
  })
  void getAppearanceFromFoundUsername_Success(String username, int expectedResult) {
    // When
    int result = UserUtils.getAppearanceFromFoundUsername(username);

    // Then
    assertEquals(expectedResult, result);
  }

  @ParameterizedTest
  @MethodSource("argumentsForBuildUsername")
  void buildUsername_Success(User user, long numOfAppearance, String expectedResult) {
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
    User user1 = TraineeTestUtil.getTrainee1();
    User user2 = TrainerTestUtil.getTrainer2();
    User user3 = TraineeTestUtil.getTrainee2();

    return Stream.of(
        Arguments.of(user1, 0, "Michael.Patel"),
        Arguments.of(user2, 3, "John.Doe3"),
        Arguments.of(user3, 4, "Michael.Patel4")
    );
  }
}
