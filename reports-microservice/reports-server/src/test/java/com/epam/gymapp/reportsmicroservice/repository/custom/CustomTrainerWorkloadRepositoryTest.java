package com.epam.gymapp.reportsmicroservice.repository.custom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.epam.gymapp.reportsmicroservice.config.TestMongoConfiguration;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.test.utils.TrainerWorkloadTestUtil;
import jakarta.annotation.PostConstruct;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;

@DataMongoTest
@ContextConfiguration(classes = TestMongoConfiguration.class)
public class CustomTrainerWorkloadRepositoryTest {

  @Autowired
  private CustomTrainerWorkloadRepository customTrainerWorkloadRepository;

  @Autowired
  private MongoTemplate mongoTemplate;

  @PostConstruct
  public void init() {
    TrainerWorkload trainerWorkload1 = TrainerWorkloadTestUtil.getTrainerWorkload1();
    TrainerWorkload trainerWorkload2 = TrainerWorkloadTestUtil.getTrainerWorkload2();

    mongoTemplate.save(trainerWorkload1);
    mongoTemplate.save(trainerWorkload2);
  }

  @ParameterizedTest
  @MethodSource("argumentsForFindAllByYearAndMonthAndFirstNameAndLastName")
  void findAllByYearAndMonthAndFirstNameAndLastName_Success(
      int year, Month month, String firstName, String lastName, List<TrainerWorkload> expectedResult) {

    // When
    List<TrainerWorkload> result = customTrainerWorkloadRepository
        .findAllByYearAndMonthAndFirstNameAndLastName(year, month, firstName, lastName);

    // Then
    assertThat(result, notNullValue());
    assertThat(result, hasSize(expectedResult.size()));
    assertEquals(expectedResult, result);
  }

  static Stream<Arguments> argumentsForFindAllByYearAndMonthAndFirstNameAndLastName() {
    TrainerWorkload trainerWorkload1 = TrainerWorkloadTestUtil.getTrainerWorkload1();
    TrainerWorkload trainerWorkload2 = TrainerWorkloadTestUtil.getTrainerWorkload2();

    return Stream.of(
        Arguments.of(2024, Month.of(4), null, null,
            List.of(trainerWorkload1, trainerWorkload2)),
        Arguments.of(2025, Month.of(5), trainerWorkload2.getFirstName(), null,
            Collections.singletonList(trainerWorkload2)),
        Arguments.of(2024, Month.of(3), trainerWorkload1.getFirstName(), trainerWorkload1.getLastName(),
            Collections.singletonList(trainerWorkload1))
    );
  }
}
