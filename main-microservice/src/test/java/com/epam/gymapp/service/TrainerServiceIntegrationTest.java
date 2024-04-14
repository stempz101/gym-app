package com.epam.gymapp.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.epam.gymapp.dto.TrainerWorkloadDto;
import com.epam.gymapp.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.exception.TrainerWorkingHoursUpdateException;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@DirtiesContext
public class TrainerServiceIntegrationTest {

  @Autowired
  private TrainerService trainerService;

  @Autowired
  private CircuitBreakerRegistry circuitBreakerRegistry;

  @RegisterExtension
  private static final WireMockExtension wireMockServer = WireMockExtension.newInstance()
      .options(wireMockConfig().dynamicPort())
      .build();

  @DynamicPropertySource
  private static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("application.reports-microservice.base-url", wireMockServer::baseUrl);
  }

  @BeforeEach
  void setUp() {
    WireMock.configureFor(wireMockServer.getPort());
    circuitBreakerRegistry.circuitBreaker("reportsMicroserviceCircuitBreaker").reset();
    trainerService.setWebClientBuilder(WebClient.builder());
  }

  @Test
  void retrieveTrainersWorkloadForMonth_Success() throws JsonProcessingException {
    // Given
    int year = 2024;
    int month = 4;
    TrainerWorkloadDto trainerWorkloadDto1 = TrainerTestUtil
        .getTrainerWorkloadDto1(2024, 4, 120);
    TrainerWorkloadDto trainerWorkloadDto2 = TrainerTestUtil
        .getTrainerWorkloadDto2(2024, 4, 340);
    List<TrainerWorkloadDto> expectedResult = List.of(trainerWorkloadDto1, trainerWorkloadDto2);

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedBody = objectMapper.writeValueAsString(expectedResult);

    // When
    wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/api/trainer-workload"))
        .withQueryParam("year", equalTo(String.valueOf(year)))
        .withQueryParam("month", equalTo(String.valueOf(month)))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedBody)));

    List<TrainerWorkloadDto> result = trainerService
        .retrieveTrainersWorkloadForMonth(year, month, null);

    // Then
    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(trainerWorkloadDto1, trainerWorkloadDto2));
  }

  @Test
  void retrieveTrainersWorkloadForMonth_WithUsername_Success() throws JsonProcessingException {
    // Given
    int year = 2024;
    int month = 4;
    String username = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    TrainerWorkloadDto trainerWorkloadDto1 = TrainerTestUtil
        .getTrainerWorkloadDto1(2024, 4, 120);
    List<TrainerWorkloadDto> expectedResult = Collections.singletonList(trainerWorkloadDto1);

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedBody = objectMapper.writeValueAsString(expectedResult);

    // When
    wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/api/trainer-workload"))
        .withQueryParam("year", equalTo(String.valueOf(year)))
        .withQueryParam("month", equalTo(String.valueOf(month)))
        .withQueryParam("username", equalTo(username))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedBody)));

    List<TrainerWorkloadDto> result = trainerService
        .retrieveTrainersWorkloadForMonth(year, month, username);

    // Then
    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(trainerWorkloadDto1));
  }

  @Test
  void retrieveTrainersWorkloadForMonth_RetryAndCircuitBreaker() throws JsonProcessingException {
    // Given
    int year = 2024;
    int month = 4;
    String username = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    TrainerWorkloadDto fallbackObject = TrainerWorkloadDto.getFallbackObject(year, month, username);
    List<TrainerWorkloadDto> expectedResult = Collections.singletonList(fallbackObject);

    ObjectMapper objectMapper = new ObjectMapper();
    String expectedBody = objectMapper.writeValueAsString(expectedResult);

    // When
    wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/api/trainer-workload"))
        .withQueryParam("year", equalTo(String.valueOf(year)))
        .withQueryParam("month", equalTo(String.valueOf(month)))
        .withQueryParam("username", equalTo(username))
        .willReturn(aResponse()
            .withStatus(500)
            .withHeader("Content-Type", "application/json")
            .withBody(expectedBody)));

    List<TrainerWorkloadDto> result = trainerService
        .retrieveTrainersWorkloadForMonth(year, month, username);

    // Then
    assertThat(result, hasSize(expectedResult.size()));
    assertThat(result, hasItems(fallbackObject));
  }

  @Test
  void updateTrainerWorkload_WithOneUpdate_Success() {
    // Given
    Training training = TrainingTestUtil.getTraining1();

    // When
    wireMockServer.stubFor(put(urlEqualTo("/api/trainer-workload"))
        .willReturn(aResponse()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withStatus(200)));

    trainerService.updateTrainerWorkload(training, ActionType.ADD);

    // Then
    verify(putRequestedFor(urlEqualTo("/api/trainer-workload")));
  }

  @Test
  void updateTrainerWorkload_WithOneUpdate_RetryAndCircuitBreaker() {
    // Given
    Training training = TrainingTestUtil.getTraining1();

    // When
    wireMockServer.stubFor(put(urlEqualTo("/api/trainer-workload"))
        .willReturn(aResponse()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withStatus(500)));

    // Then
    for (int i = 0; i < 3; i++) {
      assertThrows(TrainerWorkingHoursUpdateException.class,
          () -> trainerService.updateTrainerWorkload(training, ActionType.ADD));

      CircuitBreaker circuitBreaker = circuitBreakerRegistry
          .circuitBreaker("reportsMicroserviceCircuitBreaker");
      if (i < 2) {
        assertSame(State.CLOSED, circuitBreaker.getState());
      } else {
        assertSame(State.OPEN, circuitBreaker.getState());
      }
    }
  }

  @Test
  void updateTrainerWorkload_WithListOfUpdates_Success() {
    // Given
    List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos =
        Collections.singletonList(TrainerTestUtil.getTrainerWorkloadUpdateDto1(ActionType.ADD));

    // When
    wireMockServer.stubFor(put(urlEqualTo("/api/trainer-workload"))
        .willReturn(aResponse()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withStatus(200)));

    trainerService.updateTrainerWorkload(trainerWorkloadUpdateDtos);

    // Then
    verify(putRequestedFor(urlEqualTo("/api/trainer-workload")));
  }

  @Test
  void updateTrainerWorkload_WithListOfUpdates_RetryAndCircuitBreaker() {
    // Given
    List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos =
        Collections.singletonList(TrainerTestUtil.getTrainerWorkloadUpdateDto1(ActionType.ADD));

    // When
    wireMockServer.stubFor(put(urlEqualTo("/api/trainer-workload"))
        .willReturn(aResponse()
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withStatus(500)));

    // Then
    for (int i = 0; i < 3; i++) {
      assertThrows(TrainerWorkingHoursUpdateException.class,
          () -> trainerService.updateTrainerWorkload(trainerWorkloadUpdateDtos));

      CircuitBreaker circuitBreaker = circuitBreakerRegistry
          .circuitBreaker("reportsMicroserviceCircuitBreaker");
      if (i < 2) {
        assertSame(State.CLOSED, circuitBreaker.getState());
      } else {
        assertSame(State.OPEN, circuitBreaker.getState());
      }
    }
  }
}
