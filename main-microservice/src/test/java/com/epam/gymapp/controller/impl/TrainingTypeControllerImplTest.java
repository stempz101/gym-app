package com.epam.gymapp.controller.impl;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.config.TestSecurityConfiguration;
import com.epam.gymapp.model.SessionUser;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.model.User;
import com.epam.gymapp.repository.SessionUserRepository;
import com.epam.gymapp.repository.UserRepository;
import com.epam.gymapp.service.LoggingService;
import com.epam.gymapp.service.TrainingTypeService;
import com.epam.gymapp.test.utils.JwtTokenTestUtil;
import com.epam.gymapp.test.utils.TrainingTypeTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = TrainingTypeControllerImpl.class)
@ContextConfiguration(classes = TestSecurityConfiguration.class)
public class TrainingTypeControllerImplTest {

  @MockBean
  private TrainingTypeService trainingTypeService;

  @SpyBean
  private LoggingService loggingService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private SessionUserRepository sessionUserRepository;

  @Autowired
  private JwtTokenTestUtil jwtTokenTestUtil;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void selectTrainingTypes_Success() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    List<TrainingType> expectedResult = TrainingTypeTestUtil.getTrainingTypes();

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainingTypeService.selectTrainingTypes()).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/training-types")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(3)),

            // Item #1
            jsonPath("$[0].id").value(expectedResult.get(0).getId()),
            jsonPath("$[0].name").value(expectedResult.get(0).getName()),

            // Item #2
            jsonPath("$[1].id").value(expectedResult.get(1).getId()),
            jsonPath("$[1].name").value(expectedResult.get(1).getName()),

            // Item #3
            jsonPath("$[2].id").value(expectedResult.get(2).getId()),
            jsonPath("$[2].name").value(expectedResult.get(2).getName())
        );
  }

  @Test
  void selectTrainingTypes_NoJwtToken_Failure() throws Exception {
    // When
    ResultActions result = mockMvc.perform(get("/api/training-types"));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isUnauthorized(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Full authentication is required to access this resource")
        );
  }

  @Test
  void selectTrainingTypes_JwtTokenRevoked_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/training-types")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isUnauthorized(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Full authentication is required to access this resource")
        );
  }

  @Test
  void selectTrainingTypes_JwtTokenExpired_Failure() throws Exception {
    // Given
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/training-types")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isUnauthorized(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value(notNullValue())
        );
  }

  @Test
  void selectTrainingTypes_IfException_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainingTypeService.selectTrainingTypes()).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/training-types")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
