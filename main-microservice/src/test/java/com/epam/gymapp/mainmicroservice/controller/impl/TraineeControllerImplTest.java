package com.epam.gymapp.mainmicroservice.controller.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.mainmicroservice.config.TestSecurityConfiguration;
import com.epam.gymapp.mainmicroservice.dto.TraineeInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.exception.TraineeNotFoundException;
import com.epam.gymapp.mainmicroservice.exception.TrainerNotFoundException;
import com.epam.gymapp.mainmicroservice.exception.TrainerWorkloadUpdateException;
import com.epam.gymapp.mainmicroservice.model.SessionUser;
import com.epam.gymapp.mainmicroservice.model.User;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.repository.UserRepository;
import com.epam.gymapp.mainmicroservice.service.LoggingService;
import com.epam.gymapp.mainmicroservice.service.TraineeService;
import com.epam.gymapp.mainmicroservice.test.utils.JwtTokenTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TraineeTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainingTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainingTypeTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.UserTestUtil;
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
@WebMvcTest(value = TraineeControllerImpl.class)
@ContextConfiguration(classes = TestSecurityConfiguration.class)
public class TraineeControllerImplTest {

  @MockBean
  private TraineeService traineeService;

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
  void selectTrainees_Success() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    List<TraineeInfoDto> expectedResult = TraineeTestUtil.getTraineeInfoDtos();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(traineeService.selectTrainees()).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainees")
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
            jsonPath("$[0].dateOfBirth").value(expectedResult.get(0).getDateOfBirth()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)),
            jsonPath("$[0].address").value(expectedResult.get(0).getAddress()),
            jsonPath("$[0].active").value(expectedResult.get(0).isActive()),
            jsonPath("$[0].trainers").value(hasSize(1)),
            jsonPath("$[0].trainers[0].username")
                .value(expectedResult.get(0).getTrainers().get(0).getUsername()),
            jsonPath("$[0].trainers[0].firstName")
                .value(expectedResult.get(0).getTrainers().get(0).getFirstName()),
            jsonPath("$[0].trainers[0].lastName")
                .value(expectedResult.get(0).getTrainers().get(0).getLastName()),
            jsonPath("$[0].trainers[0].specialization")
                .value(expectedResult.get(0).getTrainers().get(0).getSpecialization()),

            // Item #2
            jsonPath("$[1].firstName").value(expectedResult.get(1).getFirstName()),
            jsonPath("$[1].lastName").value(expectedResult.get(1).getLastName()),
            jsonPath("$[1].dateOfBirth").value(expectedResult.get(1).getDateOfBirth()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)),
            jsonPath("$[1].address").value(expectedResult.get(1).getAddress()),
            jsonPath("$[1].active").value(expectedResult.get(1).isActive()),
            jsonPath("$[1].trainers").value(hasSize(2)),
            jsonPath("$[1].trainers[0].username")
                .value(expectedResult.get(1).getTrainers().get(0).getUsername()),
            jsonPath("$[1].trainers[0].firstName")
                .value(expectedResult.get(1).getTrainers().get(0).getFirstName()),
            jsonPath("$[1].trainers[0].lastName")
                .value(expectedResult.get(1).getTrainers().get(0).getLastName()),
            jsonPath("$[1].trainers[0].specialization")
                .value(expectedResult.get(1).getTrainers().get(0).getSpecialization()),
            jsonPath("$[1].trainers[1].username")
                .value(expectedResult.get(1).getTrainers().get(1).getUsername()),
            jsonPath("$[1].trainers[1].firstName")
                .value(expectedResult.get(1).getTrainers().get(1).getFirstName()),
            jsonPath("$[1].trainers[1].lastName")
                .value(expectedResult.get(1).getTrainers().get(1).getLastName()),
            jsonPath("$[1].trainers[1].specialization")
                .value(expectedResult.get(1).getTrainers().get(1).getSpecialization()),

            // Item #3
            jsonPath("$[2].firstName").value(expectedResult.get(2).getFirstName()),
            jsonPath("$[2].lastName").value(expectedResult.get(2).getLastName()),
            jsonPath("$[2].dateOfBirth").value(expectedResult.get(2).getDateOfBirth()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)),
            jsonPath("$[2].address").value(expectedResult.get(2).getAddress()),
            jsonPath("$[2].active").value(expectedResult.get(2).isActive()),
            jsonPath("$[2].trainers").value(hasSize(2)),
            jsonPath("$[2].trainers[0].username")
                .value(expectedResult.get(2).getTrainers().get(0).getUsername()),
            jsonPath("$[2].trainers[0].firstName")
                .value(expectedResult.get(2).getTrainers().get(0).getFirstName()),
            jsonPath("$[2].trainers[0].lastName")
                .value(expectedResult.get(2).getTrainers().get(0).getLastName()),
            jsonPath("$[2].trainers[0].specialization")
                .value(expectedResult.get(2).getTrainers().get(0).getSpecialization()),
            jsonPath("$[2].trainers[1].username")
                .value(expectedResult.get(2).getTrainers().get(1).getUsername()),
            jsonPath("$[2].trainers[1].firstName")
                .value(expectedResult.get(2).getTrainers().get(1).getFirstName()),
            jsonPath("$[2].trainers[1].lastName")
                .value(expectedResult.get(2).getTrainers().get(1).getLastName()),
            jsonPath("$[2].trainers[1].specialization")
                .value(expectedResult.get(2).getTrainers().get(1).getSpecialization())
        );
  }

  @Test
  void selectTrainees_NoJwtToken_Failure() throws Exception {
    // When
    ResultActions result = mockMvc.perform(get("/api/trainees"));

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
  void selectTrainees_JwtTokenRevoked_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainees")
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
  void selectTrainees_JwtTokenExpired_Failure() throws Exception {
    // Given
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/trainees")
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
  void selectTrainees_IfException_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.selectTrainees()).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void selectTrainee_Success() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1();

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.selectTrainee(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.firstName").value(expectedResult.getFirstName()),
            jsonPath("$.lastName").value(expectedResult.getLastName()),
            jsonPath("$.dateOfBirth").value(expectedResult.getDateOfBirth()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)),
            jsonPath("$.address").value(expectedResult.getAddress()),
            jsonPath("$.active").value(expectedResult.isActive()),
            jsonPath("$.trainers").value(hasSize(1)),
            jsonPath("$.trainers[0].username")
                .value(expectedResult.getTrainers().get(0).getUsername()),
            jsonPath("$.trainers[0].firstName")
                .value(expectedResult.getTrainers().get(0).getFirstName()),
            jsonPath("$.trainers[0].lastName")
                .value(expectedResult.getTrainers().get(0).getLastName()),
            jsonPath("$.trainers[0].specialization")
                .value(expectedResult.getTrainers().get(0).getSpecialization())
        );
  }

  @Test
  void selectTrainee_NoJwtToken_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;

    // When
    ResultActions result = mockMvc.perform(get("/api/trainees/" + traineeUsername));

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
  void selectTrainee_JwtTokenRevoked_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainees/" + traineeUsername)
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
  void selectTrainee_JwtTokenExpired_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/trainees/" + traineeUsername)
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
  void selectTrainee_TraineeNotFound_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.selectTrainee(any())).thenThrow(new TraineeNotFoundException(traineeUsername));

    ResultActions result = mockMvc.perform(get("/api/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainee with username '%s' is not found", traineeUsername))
        );
  }

  @Test
  void selectTrainee_IfException_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.selectTrainee(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void updateTrainee_Success() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1AfterUpdate();

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.updateTrainee(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.username").value(expectedResult.getUsername()),
            jsonPath("$.firstName").value(expectedResult.getFirstName()),
            jsonPath("$.lastName").value(expectedResult.getLastName()),
            jsonPath("$.dateOfBirth").value(expectedResult.getDateOfBirth()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)),
            jsonPath("$.address").value(expectedResult.getAddress()),
            jsonPath("$.active").value(expectedResult.isActive()),
            jsonPath("$.trainers").value(hasSize(1)),
            jsonPath("$.trainers[0].username")
                .value(expectedResult.getTrainers().get(0).getUsername()),
            jsonPath("$.trainers[0].firstName")
                .value(expectedResult.getTrainers().get(0).getFirstName()),
            jsonPath("$.trainers[0].lastName")
                .value(expectedResult.getTrainers().get(0).getLastName()),
            jsonPath("$.trainers[0].specialization")
                .value(expectedResult.getTrainers().get(0).getSpecialization())
        );
  }

  @Test
  void updateTrainee_UsernameIsNull_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setUsername(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

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
  void updateTrainee_UsernameIsEmpty_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setUsername("");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

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
  void updateTrainee_UsernameIsBlank_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setUsername("    ");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

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
  void updateTrainee_FirstNameIsNull_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setFirstName(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's first name must be specified")
        );
  }

  @Test
  void updateTrainee_FirstNameIsEmpty_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setFirstName("");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's first name must be specified")
        );
  }

  @Test
  void updateTrainee_FirstNameIsBlank_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setFirstName("    ");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's first name must be specified")
        );
  }

  @Test
  void updateTrainee_LastNameIsNull_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setLastName(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's last name must be specified")
        );
  }

  @Test
  void updateTrainee_LastNameIsEmpty_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setLastName("");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's last name must be specified")
        );
  }

  @Test
  void updateTrainee_LastNameIsBlank_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setLastName("    ");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's last name must be specified")
        );
  }

  @Test
  void updateTrainee_ActiveIsNull_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setIsActive(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

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
  void updateTrainee_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    traineeUpdateDto.setUsername(null);
    traineeUpdateDto.setFirstName(null);
    traineeUpdateDto.setLastName(null);
    traineeUpdateDto.setIsActive(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

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
                    "Trainee's first name must be specified",
                    "Trainee's last name must be specified",
                    "Activation status must be specified"
                ))
        );
  }

  @Test
  void updateTrainee_NoJwtToken_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();

    // When
    ResultActions result = mockMvc.perform(put("/api/trainees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

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
  void updateTrainee_JwtTokenRevoked_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

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
  void updateTrainee_JwtTokenExpired_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

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
  void updateTrainee_TraineeNotFound_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.updateTrainee(any()))
        .thenThrow(new TraineeNotFoundException(traineeUpdateDto.getUsername()));

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainee with username '%s' is not found",
                    traineeUpdateDto.getUsername()))
        );
  }

  @Test
  void updateTrainee_IfException_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.updateTrainee(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(put("/api/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void deleteTrainee_Success() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    doNothing().when(traineeService).deleteTrainee(any());

    ResultActions result = mockMvc.perform(delete("/api/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(status().isOk());
  }

  @Test
  void deleteTrainee_NoJwtToken_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;

    // When
    ResultActions result = mockMvc.perform(delete("/api/trainees/" + traineeUsername));

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
  void deleteTrainee_JwtTokenRevoked_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(delete("/api/trainees/" + traineeUsername)
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
  void deleteTrainee_JwtTokenExpired_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(delete("/api/trainees/" + traineeUsername)
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
  void deleteTrainee_TraineeNotFound_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    doThrow(new TraineeNotFoundException(traineeUsername)).when(traineeService).deleteTrainee(any());

    ResultActions result = mockMvc.perform(delete("/api/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainee with username '%s' is not found", traineeUsername))
        );
  }

  @Test
  void deleteTrainee_TrainerWorkloadUpdateFailed_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    doThrow(new TrainerWorkloadUpdateException()).when(traineeService).deleteTrainee(any());

    ResultActions result = mockMvc.perform(delete("/api/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isRequestTimeout(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value("Failed to update trainers' workload. Please, try again after some time!")
        );
  }

  @Test
  void deleteTrainee_IfException_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    doThrow(RuntimeException.class).when(traineeService).deleteTrainee(any());

    ResultActions result = mockMvc.perform(delete("/api/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void updateTrainerListOfTrainee_Success() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    List<TrainerShortInfoDto> expectedResult = TraineeTestUtil.getTraineeUpdatedTrainerShortInfoDtosList2();

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.updateTrainerList(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(2)),
            jsonPath("$[0].username").value(expectedResult.get(0).getUsername()),
            jsonPath("$[0].firstName").value(expectedResult.get(0).getFirstName()),
            jsonPath("$[0].lastName").value(expectedResult.get(0).getLastName()),
            jsonPath("$[0].specialization").value(expectedResult.get(0).getSpecialization()),
            jsonPath("$[1].username").value(expectedResult.get(1).getUsername()),
            jsonPath("$[1].firstName").value(expectedResult.get(1).getFirstName()),
            jsonPath("$[1].lastName").value(expectedResult.get(1).getLastName()),
            jsonPath("$[1].specialization").value(expectedResult.get(1).getSpecialization())
        );
  }

  @Test
  void updateTrainerListOfTrainee_TraineeUsernameIsNull_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTraineeUsername(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's username must be specified")
        );
  }

  @Test
  void updateTrainerListOfTrainee_TraineeUsernameIsEmpty_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTraineeUsername("");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's username must be specified")
        );
  }

  @Test
  void updateTrainerListOfTrainee_TraineeUsernameIsBlank_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTraineeUsername("    ");
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainee's username must be specified")
        );
  }

  @Test
  void updateTrainerListOfTrainee_TrainerUsernameListIsNull_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTrainerUsernames(null);
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainers list must be filled")
        );
  }

  @Test
  void updateTrainerListOfTrainee_TrainerUsernameListIsEmpty_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTrainerUsernames(Collections.emptyList());
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value("At least one trainer must be specified in list")
        );
  }

  @Test
  void updateTrainerListOfTrainee_TrainerUsernameIsNullInList_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTrainerUsernames(Collections.singletonList(null));
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value("Trainer's username must be specified in list")
        );
  }

  @Test
  void updateTrainerListOfTrainee_TrainerUsernameIsEmptyInList_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTrainerUsernames(Collections.singletonList(""));
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value("Trainer's username must be specified in list")
        );
  }

  @Test
  void updateTrainerListOfTrainee_TrainerUsernameIsBlankInList_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTrainerUsernames(Collections.singletonList("   "));
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value("Trainer's username must be specified in list")
        );
  }

  @Test
  void updateTrainerListOfTrainee_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    traineeTrainersUpdateDto.setTraineeUsername(null);
    traineeTrainersUpdateDto.setTrainerUsernames(Collections.emptyList());
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(2)),
            jsonPath("$[*].message")
                .value(containsInAnyOrder(
                    "Trainee's username must be specified",
                    "At least one trainer must be specified in list"
                ))
        );
  }

  @Test
  void updateTrainerListOfTrainee_NoJwtToken_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();

    // When
    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

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
  void updateTrainerListOfTrainee_JwtTokenRevoked_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

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
  void updateTrainerListOfTrainee_JwtTokenExpired_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

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
  void updateTrainerListOfTrainee_TraineeNotFound_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.updateTrainerList(any()))
        .thenThrow(new TraineeNotFoundException(traineeTrainersUpdateDto.getTraineeUsername()));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainee with username '%s' is not found",
                    traineeTrainersUpdateDto.getTraineeUsername()))
        );
  }

  @Test
  void updateTrainerListOfTrainee_TrainersNotFound_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.updateTrainerList(any()))
        .thenThrow(new TrainerNotFoundException(traineeTrainersUpdateDto.getTrainerUsernames()));

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainers are not found by these usernames: %s",
                    traineeTrainersUpdateDto.getTrainerUsernames()))
        );
  }

  @Test
  void updateTrainerListOfTrainee_IfException_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.updateTrainerList(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(put("/api/trainees/trainers")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeTrainersUpdateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void getTraineeTrainings_Success() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3);
    String trainingType = TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_3;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    List<TrainingInfoDto> expectedResult = List.of(TrainingTestUtil.getTrainingInfoDto2());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.findTraineeTrainings(any(), any(), any(), any(), any()))
        .thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainees/trainings")
        .param("username", traineeUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("trainerName", trainerName)
        .param("trainingType", trainingType)
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
            jsonPath("$[0].trainerName").value(expectedResult.get(0).getTrainerName())
        );
  }

  @Test
  void getTraineeTrainings_MissingRequiredParameters_Failure() throws Exception {
    // Given
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(get("/api/trainees/trainings")
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
  void getTraineeTrainings_FromDateNotParsed_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    String fromDate = "some text";
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(get("/api/trainees/trainings")
        .param("username", traineeUsername)
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
  void getTraineeTrainings_ToDateNotParsed_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    String toDate = "some text";
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(get("/api/trainees/trainings")
        .param("username", traineeUsername)
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
  void getTraineeTrainings_NoJwtToken_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3);
    String trainingType = TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_3;

    // When
    ResultActions result = mockMvc.perform(get("/api/trainees/trainings")
        .param("username", traineeUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("trainerName", trainerName)
        .param("trainingType", trainingType));

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
  void getTraineeTrainings_JwtTokenRevoked_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3);
    String trainingType = TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_3;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

    ResultActions result = mockMvc.perform(get("/api/trainees/trainings")
        .param("username", traineeUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("trainerName", trainerName)
        .param("trainingType", trainingType)
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
  void getTraineeTrainings_JwtTokenExpired_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3);
    String trainingType = TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_3;
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/api/trainees/trainings")
        .param("username", traineeUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("trainerName", trainerName)
        .param("trainingType", trainingType)
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
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3);
    String trainingType = TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_3;
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(traineeService.findTraineeTrainings(any(), any(), any(), any(), any()))
        .thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainees/trainings")
        .param("username", traineeUsername)
        .param("fromDate", fromDate)
        .param("toDate", toDate)
        .param("trainerName", trainerName)
        .param("trainingType", trainingType)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
