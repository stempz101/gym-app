package com.epam.gymapp.mainmicroservice.controller.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.mainmicroservice.config.TestSecurityConfiguration;
import com.epam.gymapp.mainmicroservice.dto.TrainerInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.model.SessionUser;
import com.epam.gymapp.mainmicroservice.model.User;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.repository.UserRepository;
import com.epam.gymapp.mainmicroservice.service.LoggingService;
import com.epam.gymapp.mainmicroservice.service.TrainerService;
import com.epam.gymapp.mainmicroservice.test.utils.JwtTokenTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainerTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainingTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.UserTestUtil;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
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
@WebMvcTest(value = TrainerControllerImpl.class)
@ContextConfiguration(classes = TestSecurityConfiguration.class)
public class TrainerControllerImplTest {

  @MockBean
  private TrainerService trainerService;

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

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void selectTrainers_Success() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    List<TrainerInfoDto> expectedResult = TrainerTestUtil.getTrainerInfoDtos();

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.selectTrainers()).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(3)),

            // Item #1
            jsonPath("$[0].firstName").value(expectedResult.get(0).getFirstName()),
            jsonPath("$[0].lastName").value(expectedResult.get(0).getLastName()),
            jsonPath("$[0].specialization").value(expectedResult.get(0).getSpecialization()),
            jsonPath("$[0].active").value(expectedResult.get(0).isActive()),
            jsonPath("$[0].trainees").value(hasSize(2)),
            jsonPath("$[0].trainees[0].username")
                .value(expectedResult.get(0).getTrainees().get(0).getUsername()),
            jsonPath("$[0].trainees[0].firstName")
                .value(expectedResult.get(0).getTrainees().get(0).getFirstName()),
            jsonPath("$[0].trainees[0].lastName")
                .value(expectedResult.get(0).getTrainees().get(0).getLastName()),
            jsonPath("$[0].trainees[1].username")
                .value(expectedResult.get(0).getTrainees().get(1).getUsername()),
            jsonPath("$[0].trainees[1].firstName")
                .value(expectedResult.get(0).getTrainees().get(1).getFirstName()),
            jsonPath("$[0].trainees[1].lastName")
                .value(expectedResult.get(0).getTrainees().get(1).getLastName()),

            // Item #2
            jsonPath("$[1].firstName").value(expectedResult.get(1).getFirstName()),
            jsonPath("$[1].lastName").value(expectedResult.get(1).getLastName()),
            jsonPath("$[1].specialization").value(expectedResult.get(1).getSpecialization()),
            jsonPath("$[1].active").value(expectedResult.get(1).isActive()),
            jsonPath("$[1].trainees").value(hasSize(2)),
            jsonPath("$[1].trainees[0].username")
                .value(expectedResult.get(1).getTrainees().get(0).getUsername()),
            jsonPath("$[1].trainees[0].firstName")
                .value(expectedResult.get(1).getTrainees().get(0).getFirstName()),
            jsonPath("$[1].trainees[0].lastName")
                .value(expectedResult.get(1).getTrainees().get(0).getLastName()),
            jsonPath("$[1].trainees[1].username")
                .value(expectedResult.get(1).getTrainees().get(1).getUsername()),
            jsonPath("$[1].trainees[1].firstName")
                .value(expectedResult.get(1).getTrainees().get(1).getFirstName()),
            jsonPath("$[1].trainees[1].lastName")
                .value(expectedResult.get(1).getTrainees().get(1).getLastName()),

            // Item #3
            jsonPath("$[2].firstName").value(expectedResult.get(2).getFirstName()),
            jsonPath("$[2].lastName").value(expectedResult.get(2).getLastName()),
            jsonPath("$[2].specialization").value(expectedResult.get(2).getSpecialization()),
            jsonPath("$[2].active").value(expectedResult.get(2).isActive()),
            jsonPath("$[2].trainees").value(hasSize(2)),
            jsonPath("$[2].trainees[0].username")
                .value(expectedResult.get(2).getTrainees().get(0).getUsername()),
            jsonPath("$[2].trainees[0].firstName")
                .value(expectedResult.get(2).getTrainees().get(0).getFirstName()),
            jsonPath("$[2].trainees[0].lastName")
                .value(expectedResult.get(2).getTrainees().get(0).getLastName()),
            jsonPath("$[2].trainees[1].username")
                .value(expectedResult.get(2).getTrainees().get(1).getUsername()),
            jsonPath("$[2].trainees[1].firstName")
                .value(expectedResult.get(2).getTrainees().get(1).getFirstName()),
            jsonPath("$[2].trainees[1].lastName")
                .value(expectedResult.get(2).getTrainees().get(1).getLastName())
        );
  }

  @Test
  void selectTrainers_NoJwtToken_Failure() throws Exception {
    // When
    ResultActions result = mockMvc.perform(get("/api/trainers"));

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
  void selectTrainers_JwtTokenRevoked_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainers")
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
  void selectTrainers_JwtTokenExpired_Failure() throws Exception {
    // Given
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers")
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
  void selectTrainers_IfException_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.selectTrainers()).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void selectTrainer_Success() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    TrainerInfoDto expectedResult = TrainerTestUtil.getTrainerInfoDto1();

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.selectTrainer(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainers/" + trainerUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.firstName").value(expectedResult.getFirstName()),
            jsonPath("$.lastName").value(expectedResult.getLastName()),
            jsonPath("$.specialization").value(expectedResult.getSpecialization()),
            jsonPath("$.active").value(expectedResult.isActive()),
            jsonPath("$.trainees").value(hasSize(2)),
            jsonPath("$.trainees[0].username")
                .value(expectedResult.getTrainees().get(0).getUsername()),
            jsonPath("$.trainees[0].firstName")
                .value(expectedResult.getTrainees().get(0).getFirstName()),
            jsonPath("$.trainees[0].lastName")
                .value(expectedResult.getTrainees().get(0).getLastName()),
            jsonPath("$.trainees[1].username")
                .value(expectedResult.getTrainees().get(1).getUsername()),
            jsonPath("$.trainees[1].firstName")
                .value(expectedResult.getTrainees().get(1).getFirstName()),
            jsonPath("$.trainees[1].lastName")
                .value(expectedResult.getTrainees().get(1).getLastName())
        );
  }

  @Test
  void selectTrainer_NoJwtToken_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers/" + trainerUsername));

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
  void selectTrainer_JwtTokenRevoked_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainers/" + trainerUsername)
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
  void selectTrainer_JwtTokenExpired_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers/" + trainerUsername)
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
  void selectTrainer_TraineeNotFound_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.selectTrainer(any())).thenThrow(new TrainerNotFoundException(trainerUsername));

    ResultActions result = mockMvc.perform(get("/api/trainers/" + trainerUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainer with username '%s' is not found", trainerUsername))
        );
  }

  @Test
  void selectTrainer_IfException_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.selectTrainer(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainers/" + trainerUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void updateTrainer_Success() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    TrainerInfoDto expectedResult = TrainerTestUtil.getTrainerInfoDto1AfterUpdate();

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.updateTrainer(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.username").value(expectedResult.getUsername()),
            jsonPath("$.firstName").value(expectedResult.getFirstName()),
            jsonPath("$.lastName").value(expectedResult.getLastName()),
            jsonPath("$.specialization").value(expectedResult.getSpecialization()),
            jsonPath("$.active").value(expectedResult.isActive()),
            jsonPath("$.trainees").value(hasSize(2)),
            jsonPath("$.trainees[0].username")
                .value(expectedResult.getTrainees().get(0).getUsername()),
            jsonPath("$.trainees[0].firstName")
                .value(expectedResult.getTrainees().get(0).getFirstName()),
            jsonPath("$.trainees[0].lastName")
                .value(expectedResult.getTrainees().get(0).getLastName()),
            jsonPath("$.trainees[1].username")
                .value(expectedResult.getTrainees().get(1).getUsername()),
            jsonPath("$.trainees[1].firstName")
                .value(expectedResult.getTrainees().get(1).getFirstName()),
            jsonPath("$.trainees[1].lastName")
                .value(expectedResult.getTrainees().get(1).getLastName())
        );
  }

  @Test
  void updateTrainer_UsernameIsNull_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setUsername(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Username must be specified")
        );
  }

  @Test
  void updateTrainer_UsernameIsEmpty_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setUsername("");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Username must be specified")
        );
  }

  @Test
  void updateTrainer_UsernameIsBlank_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setUsername("    ");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Username must be specified")
        );
  }

  @Test
  void updateTrainer_FirstNameIsNull_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setFirstName(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_FirstNameIsEmpty_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setFirstName("");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_FirstNameIsBlank_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setFirstName("    ");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_LastNameIsNull_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setLastName(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_LastNameIsEmpty_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setLastName("");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_LastNameIsBlank_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setLastName("    ");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_ActiveIsNull_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setIsActive(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Activation status must be specified")
        );
  }

  @Test
  void updateTrainer_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    trainerUpdateDto.setUsername(null);
    trainerUpdateDto.setFirstName(null);
    trainerUpdateDto.setLastName(null);
    trainerUpdateDto.setIsActive(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(4)),
            jsonPath("$[*].message")
                .value(containsInAnyOrder(
                    "Username must be specified",
                    "Trainer's first name must be specified",
                    "Trainer's last name must be specified",
                    "Activation status must be specified"
                ))
        );
  }

  @Test
  void updateTrainer_NoJwtToken_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();

    // When
    ResultActions result = mockMvc.perform(put("/api/trainers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_JwtTokenRevoked_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_JwtTokenExpired_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

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
  void updateTrainer_TrainerNotFound_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.updateTrainer(any()))
        .thenThrow(new TrainerNotFoundException(trainerUpdateDto.getUsername()));

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainer with username '%s' is not found",
                    trainerUpdateDto.getUsername()))
        );
  }

  @Test
  void updateTrainer_IfException_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.updateTrainer(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(put("/api/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void getUnassignedTrainersOnTrainee_Success() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    List<TrainerShortInfoDto> expectedResult = TrainerTestUtil.getUnassignedTrainerShortInfoDtosOnTrainee1();

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.findUnassignedTrainers(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainers/unassigned/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(3)),

            // Item #1
            jsonPath("$[0].username").value(expectedResult.get(0).getUsername()),
            jsonPath("$[0].firstName").value(expectedResult.get(0).getFirstName()),
            jsonPath("$[0].lastName").value(expectedResult.get(0).getLastName()),
            jsonPath("$[0].specialization").value(expectedResult.get(0).getSpecialization()),

            // Item #2
            jsonPath("$[1].username").value(expectedResult.get(1).getUsername()),
            jsonPath("$[1].firstName").value(expectedResult.get(1).getFirstName()),
            jsonPath("$[1].lastName").value(expectedResult.get(1).getLastName()),
            jsonPath("$[1].specialization").value(expectedResult.get(1).getSpecialization()),

            // Item #3
            jsonPath("$[2].username").value(expectedResult.get(2).getUsername()),
            jsonPath("$[2].firstName").value(expectedResult.get(2).getFirstName()),
            jsonPath("$[2].lastName").value(expectedResult.get(2).getLastName()),
            jsonPath("$[2].specialization").value(expectedResult.get(2).getSpecialization())
        );
  }

  @Test
  void getUnassignedTrainersOnTrainee_NoJwtToken_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers/unassigned/" + traineeUsername));

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
  void getUnassignedTrainersOnTrainee_JwtTokenRevoked_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainers/unassigned/" + traineeUsername)
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
  void getUnassignedTrainersOnTrainee_JwtTokenExpired_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers/unassigned/" + traineeUsername)
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
  void getUnassignedTrainersOnTrainee_IfException_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.findUnassignedTrainers(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainers/unassigned/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void getTrainerTrainings_Success() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    List<TrainingInfoDto> expectedResult = List.of(TrainingTestUtil.getTrainingInfoDto1());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.findTrainerTrainings(any(), any(), any(), any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainers/trainings")
        .param("username", trainerUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("traineeName", traineeName)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].name").value(expectedResult.get(0).getName()),
            jsonPath("$[0].date").value(expectedResult.get(0).getDate()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)),
            jsonPath("$[0].type").value(expectedResult.get(0).getType()),
            jsonPath("$[0].duration").value(expectedResult.get(0).getDuration()),
            jsonPath("$[0].traineeName").value(expectedResult.get(0).getTraineeName())
        );
  }

  @Test
  void getTrainerTrainings_MissingRequiredParameters_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(get("/api/trainers/trainings")
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
  void getTrainerTrainings_FromDateNotParsed_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    String fromDate = "some text";
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(get("/api/trainers/trainings")
        .param("username", trainerUsername)
        .param("fromDate", fromDate)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Cannot parse String '%s' to LocalDate", fromDate))
        );
  }

  @Test
  void getTrainerTrainings_ToDateNotParsed_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    String toDate = "some text";
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(get("/api/trainers/trainings")
        .param("username", trainerUsername)
        .param("toDate", toDate)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Cannot parse String '%s' to LocalDate", toDate))
        );
  }

  @Test
  void getTrainerTrainings_NoJwtToken_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1);

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers/trainings")
        .param("username", trainerUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("traineeName", traineeName));

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
  void getTrainerTrainings_JwtTokenRevoked_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainers/trainings")
        .param("username", trainerUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("traineeName", traineeName)
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
  void getTrainerTrainings_JwtTokenExpired_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1);
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers/trainings")
        .param("username", trainerUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("traineeName", traineeName)
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
  void getTraineeTrainings_IfException_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.findTrainerTrainings(any(), any(), any(), any()))
        .thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainers/trainings")
        .param("username", trainerUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("traineeName", traineeName)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void retrieveTrainersWorkloadForMonth_Success() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    String firstName = UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1;
    String lastName = UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1;
    TrainerWorkloadDto trainerWorkloadDto1 = TrainerTestUtil
        .getTrainerWorkloadDto1(2024, 4, 120);
    List<TrainerWorkloadDto> expectedResult = Collections.singletonList(trainerWorkloadDto1);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any(), any()))
        .thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainers/workload")
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month))
        .param("firstName", firstName)
        .param("lastName", lastName)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),

            // Item #1
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
  void retrieveTrainersWorkloadForMonth_MissingRequiredParameters_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(get("/api/trainers/workload")
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
  void retrieveTrainersWorkloadForMonth_NoJwtToken_Failure() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    String firstName = UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1;
    String lastName = UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1;

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers/workload")
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month))
        .param("firstName", firstName)
        .param("lastName", lastName));

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
    String firstName = UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1;
    String lastName = UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainers/workload")
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month))
        .param("firstName", firstName)
        .param("lastName", lastName)
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
  void retrieveTrainersWorkloadForMonth_JwtTokenExpired_Failure() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    String firstName = UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1;
    String lastName = UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1;
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/trainers/workload")
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month))
        .param("firstName", firstName)
        .param("lastName", lastName)
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
  void retrieveTrainersWorkloadForMonth_IfException_Failure() throws Exception {
    // Given
    int year = 2024;
    int month = 4;
    String firstName = UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_1;
    String lastName = UserTestUtil.TEST_TRAINER_USER_LAST_NAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(trainerService.retrieveTrainersWorkloadForMonth(anyInt(), anyInt(), any(), any()))
        .thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainers/workload")
        .param("year", String.valueOf(year))
        .param("month", String.valueOf(month))
        .param("firstName", firstName)
        .param("lastName", lastName)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
