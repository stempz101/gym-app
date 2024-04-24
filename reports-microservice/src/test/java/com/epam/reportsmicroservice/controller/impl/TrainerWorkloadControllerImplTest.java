package com.epam.reportsmicroservice.controller.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.reportsmicroservice.config.TestSecurityConfiguration;
import com.epam.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.reportsmicroservice.model.SessionUser;
import com.epam.reportsmicroservice.model.security.User;
import com.epam.reportsmicroservice.repository.SessionUserRepository;
import com.epam.reportsmicroservice.service.LoggingService;
import com.epam.reportsmicroservice.service.TrainerWorkloadService;
import com.epam.reportsmicroservice.test.utils.JwtTokenTestUtil;
import com.epam.reportsmicroservice.test.utils.TrainerWorkloadTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Collections;
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
@WebMvcTest(value = TrainerWorkloadControllerImpl.class)
@ContextConfiguration(classes = TestSecurityConfiguration.class)
public class TrainerWorkloadControllerImplTest {

  @MockBean
  private TrainerWorkloadService trainerWorkloadService;

  @SpyBean
  private LoggingService loggingService;

  @MockBean
  private SessionUserRepository sessionUserRepository;

  @Autowired
  private JwtTokenTestUtil jwtTokenTestUtil;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void retrieveTrainersWorkloadForMonth_Success() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    TrainerWorkloadDto trainerWorkloadDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadDto1(2024, 4, 10);
    TrainerWorkloadDto trainerWorkloadDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadDto2(2024, 4, 60);
    List<TrainerWorkloadDto> expectedResult = List.of(trainerWorkloadDto1, trainerWorkloadDto2);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(trainerWorkloadService.retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any()))
        .thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(2)),

            // Item #1
            jsonPath("$[0].username").value(expectedResult.get(0).getUsername()),
            jsonPath("$[0].firstName").value(expectedResult.get(0).getFirstName()),
            jsonPath("$[0].lastName").value(expectedResult.get(0).getLastName()),
            jsonPath("$[0].active").value(expectedResult.get(0).isActive()),
            jsonPath("$[0].year").value(expectedResult.get(0).getYear()),
            jsonPath("$[0].month").value(expectedResult.get(0).getMonth().toString()),
            jsonPath("$[0].duration").value(expectedResult.get(0).getDuration()),

            // Item #2
            jsonPath("$[1].username").value(expectedResult.get(1).getUsername()),
            jsonPath("$[1].firstName").value(expectedResult.get(1).getFirstName()),
            jsonPath("$[1].lastName").value(expectedResult.get(1).getLastName()),
            jsonPath("$[1].active").value(expectedResult.get(1).isActive()),
            jsonPath("$[1].year").value(expectedResult.get(1).getYear()),
            jsonPath("$[1].month").value(expectedResult.get(1).getMonth().toString()),
            jsonPath("$[1].duration").value(expectedResult.get(1).getDuration())
        );
  }

  @Test
  void retrieveTrainersWorkloadForMonth_WithUsername_Success() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    String username = TrainerWorkloadTestUtil.TEST_TRAINER_USERNAME_1;
    TrainerWorkloadDto trainerWorkloadDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadDto1(2024, 4, 10);
    List<TrainerWorkloadDto> expectedResult = Collections.singletonList(trainerWorkloadDto1);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(trainerWorkloadService.retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any()))
        .thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month))
        .param("username", username));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].username").value(expectedResult.get(0).getUsername()),
            jsonPath("$[0].firstName").value(expectedResult.get(0).getFirstName()),
            jsonPath("$[0].lastName").value(expectedResult.get(0).getLastName()),
            jsonPath("$[0].active").value(expectedResult.get(0).isActive()),
            jsonPath("$[0].year").value(expectedResult.get(0).getYear()),
            jsonPath("$[0].month").value(expectedResult.get(0).getMonth().toString()),
            jsonPath("$[0].duration").value(expectedResult.get(0).getDuration())
        );
  }

  @Test
  void retrieveTrainersWorkloadForMonth_NoJwtToken_Failure() throws Exception {
    // Given
    int year = 2024;
    int month = 4;

    ResultActions result = mockMvc.perform(get("/api/trainer-workload")
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month)));

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
  void retrieveTrainersWorkloadForMonth_JwtTokenRevoked_Failure() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month)));

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
  void retrieveTrainersWorkloadForMonth_JwtTokenExpired_Failure() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), new User("my_username"));

    // When
    ResultActions result = mockMvc.perform(get("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month)));

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
  void retrieveTrainersWorkloadForMonth_IfException_Failure() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(trainerWorkloadService.retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any()))
        .thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void retrieveTrainersWorkloadForMonth_MissingRequiredParameters_Failure() throws Exception {
    // Given
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(get("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value(notNullValue())
        );
  }

  @Test
  void updateTrainersRecords_Success() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    doNothing().when(trainerWorkloadService).updateTrainersWorkload(any());

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void updateTrainersRecords_UsernameIsNull_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setUsername(null);
    trainerWorkloadUpdateDto2.setUsername(null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's username must be specified")
        );
  }

  @Test
  void updateTrainersRecords_UsernameIsEmpty_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setUsername("");
    trainerWorkloadUpdateDto2.setUsername("");
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's username must be specified")
        );
  }

  @Test
  void updateTrainersRecords_UsernameIsBlank_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setUsername("   ");
    trainerWorkloadUpdateDto2.setUsername("   ");
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's username must be specified")
        );
  }

  @Test
  void updateTrainersRecords_FirstNameIsNull_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setFirstName(null);
    trainerWorkloadUpdateDto2.setFirstName(null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's first name must be specified")
        );
  }

  @Test
  void updateTrainersRecords_FirstNameIsEmpty_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setFirstName("");
    trainerWorkloadUpdateDto2.setFirstName("");
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's first name must be specified")
        );
  }

  @Test
  void updateTrainersRecords_FirstNameIsBlank_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setFirstName("   ");
    trainerWorkloadUpdateDto2.setFirstName("   ");
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's first name must be specified")
        );
  }

  @Test
  void updateTrainersRecords_LastNameIsNull_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setLastName(null);
    trainerWorkloadUpdateDto2.setLastName(null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's last name must be specified")
        );
  }

  @Test
  void updateTrainersRecords_LastNameIsEmpty_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setLastName("");
    trainerWorkloadUpdateDto2.setLastName("");
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's last name must be specified")
        );
  }

  @Test
  void updateTrainersRecords_LastNameIsBlank_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setLastName("   ");
    trainerWorkloadUpdateDto2.setLastName("   ");
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's last name must be specified")
        );
  }

  @Test
  void updateTrainersRecords_ActiveIsNull_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setIsActive(null);
    trainerWorkloadUpdateDto2.setIsActive(null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's activation status must be specified")
        );
  }

  @Test
  void updateTrainersRecords_TrainingDateIsNull_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setTrainingDate(null);
    trainerWorkloadUpdateDto2.setTrainingDate(null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Training date must be specified")
        );
  }

  @Test
  void updateTrainersRecords_TrainingDurationIsNull_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    trainerWorkloadUpdateDto1.setTrainingDuration(null);
    trainerWorkloadUpdateDto2.setTrainingDuration(null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Training duration must be specified")
        );
  }

  @Test
  void updateTrainersRecords_TrainingDurationIsNegative_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, -10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, -60, ActionType.DELETE);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Training duration must be a non-negative value")
        );
  }

  @Test
  void updateTrainersRecords_ActionTypeIsNull_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, null);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Training action type must be specified")
        );
  }

  @Test
  void updateTrainersRecords_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, null);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, null);
    trainerWorkloadUpdateDto1.setUsername(null);
    trainerWorkloadUpdateDto1.setFirstName(null);
    trainerWorkloadUpdateDto1.setLastName(null);
    trainerWorkloadUpdateDto1.setIsActive(null);
    trainerWorkloadUpdateDto2.setIsActive(null);
    trainerWorkloadUpdateDto2.setTrainingDate(null);
    trainerWorkloadUpdateDto2.setTrainingDuration(null);
    trainerWorkloadUpdateDto2.setActionType(null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(7)),
            jsonPath("$[*].message")
                .value(containsInAnyOrder(
                    "Trainer's username must be specified",
                    "Trainer's first name must be specified",
                    "Trainer's last name must be specified",
                    "Trainer's activation status must be specified",
                    "Training date must be specified",
                    "Training duration must be specified",
                    "Training action type must be specified"
                ))
        );
  }

  @Test
  void updateTrainersRecords_NoJwtToken_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, null);

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Collections.singletonList(
            trainerWorkloadUpdateDto1))));

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
  void updateTrainersRecords_JwtTokenRevoked_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, null);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Collections.singletonList(
            trainerWorkloadUpdateDto1))));

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
  void updateTrainersRecords_JwtTokenExpired_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, null);
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), new User("my_username"));

    // When
    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(Collections.singletonList(
            trainerWorkloadUpdateDto1))));

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
  void updateTrainersRecords_IfException_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto(2025, 5, 60, ActionType.DELETE);
    User user = new User("user1");
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), LocalDateTime.now());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    doThrow(RuntimeException.class).when(trainerWorkloadService).updateTrainersWorkload(any());

    ResultActions result = mockMvc.perform(put("/api/trainer-workload")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(
            List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2))));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
