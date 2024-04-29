package com.epam.gymapp.mainmicroservice.controller.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.mainmicroservice.config.TestSecurityConfiguration;
import com.epam.gymapp.mainmicroservice.dto.ChangePasswordDto;
import com.epam.gymapp.mainmicroservice.dto.JwtDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerCreateDto;
import com.epam.gymapp.mainmicroservice.dto.UserCredentialsDto;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.repository.UserRepository;
import com.epam.gymapp.mainmicroservice.service.LoggingService;
import com.epam.gymapp.mainmicroservice.service.TraineeService;
import com.epam.gymapp.mainmicroservice.service.TrainerService;
import com.epam.gymapp.mainmicroservice.service.UserService;
import com.epam.gymapp.mainmicroservice.test.utils.JwtTokenTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TraineeTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.TrainerTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.UserTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AuthenticationControllerImpl.class)
@ContextConfiguration(classes = TestSecurityConfiguration.class)
public class AuthenticationControllerImplTest {

  @SpyBean
  private UserService userService;

  @MockBean
  private TraineeService traineeService;

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
  void authenticate_Success() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    String token = jwtTokenTestUtil.generateToken(UserTestUtil.getTraineeUser1());
    JwtDto expectedResult = UserTestUtil.getUserJwtDto(token);

    // When
    doReturn(expectedResult).when(userService).authenticate(any());

    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.token").value(expectedResult.getToken())
        );
  }

  @Test
  void authenticate_UsernameIsNull_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    userCredentialsDto.setUsername(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

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
  void authenticate_UsernameIsEmpty_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    userCredentialsDto.setUsername("");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

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
  void authenticate_UsernameIsBlank_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    userCredentialsDto.setUsername("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

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
  void authenticate_PasswordIsNull_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    userCredentialsDto.setPassword(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Password must be specified")
        );
  }

  @Test
  void authenticate_PasswordIsEmpty_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    userCredentialsDto.setPassword(new char[0]);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Password must be specified")
        );
  }

  @Test
  void authenticate_BadCredentials_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();

    // When
    doThrow(new BadCredentialsException("Bad credentials")).when(userService).authenticate(any());

    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Bad credentials")
        );
  }

  @Test
  void authenticate_BruteForceAttempts_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.empty());

    mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));
    mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));
    mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));
    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isTooManyRequests(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("User is blocked from logging in due to too many failed attempts")
        );
  }

  @Test
  void authenticate_IfException_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();

    // When
    doThrow(RuntimeException.class).when(userService).authenticate(any());

    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void registerTrainee_Success() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    String token = jwtTokenTestUtil.generateToken(UserTestUtil.getTraineeUser1());
    UserCredentialsDto expectedResult = UserTestUtil.getTraineeUserCredentialsDto1WithToken(token);

    // When
    when(traineeService.createTrainee(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isCreated(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.username").value(expectedResult.getUsername()),
            jsonPath("$.password").value(String.valueOf(expectedResult.getPassword())),
            jsonPath("$.token").value(expectedResult.getToken())
        );
  }

  @Test
  void registerTrainee_FirstNameIsNull_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

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
  void registerTrainee_FirstNameIsEmpty_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName("");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

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
  void registerTrainee_FirstNameIsBlank_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

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
  void registerTrainee_LastNameIsNull_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

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
  void registerTrainee_LastNameIsEmpty_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName("");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

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
  void registerTrainee_LastNameIsBlank_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

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
  void registerTrainee_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName(null);
    traineeCreateDto.setLastName(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(2)),
            jsonPath("$[*].message")
                .value(containsInAnyOrder(
                    "Trainee's first name must be specified",
                    "Trainee's last name must be specified"
                ))
        );
  }

  @Test
  void registerTrainee_IfException_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();

    // When
    when(traineeService.createTrainee(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(post("/api/auth/trainee/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void registerTrainer_Success() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    String token = jwtTokenTestUtil.generateToken(UserTestUtil.getTrainerUser1());
    UserCredentialsDto expectedResult = UserTestUtil.getTrainerUserCredentialsDto1WithToken(token);

    // When
    when(trainerService.createTrainer(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isCreated(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.username").value(expectedResult.getUsername()),
            jsonPath("$.password").value(String.valueOf(expectedResult.getPassword())),
            jsonPath("$.token").value(expectedResult.getToken())
        );
  }

  @Test
  void registerTrainer_FirstNameIsNull_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

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
  void registerTrainer_FirstNameIsEmpty_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName("");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

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
  void registerTrainer_FirstNameIsBlank_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

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
  void registerTrainer_LastNameIsNull_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

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
  void registerTrainer_LastNameIsEmpty_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName("");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

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
  void registerTrainer_LastNameIsBlank_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

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
  void registerTrainer_SpecializationIsNull_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's specialization must be specified")
        );
  }

  @Test
  void registerTrainer_SpecializationIsEmpty_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization("");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's specialization must be specified")
        );
  }

  @Test
  void registerTrainer_SpecializationIsBlank_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Trainer's specialization must be specified")
        );
  }

  @Test
  void registerTrainer_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName(null);
    trainerCreateDto.setLastName(null);
    trainerCreateDto.setSpecialization(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(3)),
            jsonPath("$[*].message")
                .value(containsInAnyOrder(
                    "Trainer's first name must be specified",
                    "Trainer's last name must be specified",
                    "Trainer's specialization must be specified"
                ))
        );
  }

  @Test
  void registerTrainer_IfException_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();

    // When
    when(trainerService.createTrainer(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(post("/api/auth/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void changePassword_Success() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();

    // When
    doNothing().when(userService).changePassword(any());

    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void changePassword_UsernameIsNull_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setUsername(null);

    // When
    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

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
  void changePassword_UsernameIsEmpty_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setUsername("");

    // When
    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

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
  void changePassword_UsernameIsBlank_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setUsername("    ");

    // When
    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

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
  void changePassword_OldPasswordIsNull_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setOldPassword(null);

    // When
    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Current password must be specified")
        );
  }

  @Test
  void changePassword_OldPasswordIsEmpty_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setOldPassword(new char[0]);

    // When
    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Current password must be specified")
        );
  }

  @Test
  void changePassword_NewPasswordIsNull_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setNewPassword(null);

    // When
    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("New password must be specified")
        );
  }

  @Test
  void changePassword_NewPasswordIsEmpty_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setNewPassword(new char[0]);

    // When
    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("New password must be specified")
        );
  }

  @Test
  void changePassword_PasswordsAreEqual_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setNewPassword(changePasswordDto.getOldPassword());

    // When
    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value("New password must not be equal to the current one")
        );
  }

  @Test
  void changePassword_BadCredentials_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();

    // When
    doThrow(new BadCredentialsException("Specified wrong username or password"))
        .when(userService).changePassword(any());

    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Specified wrong username or password")
        );
  }

  @Test
  void changePassword_IfException_Failure() throws Exception {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();

    // When
    doThrow(RuntimeException.class).when(userService).changePassword(any());

    ResultActions result = mockMvc.perform(put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void logout_Success() throws Exception {
    // Given
    String token = jwtTokenTestUtil.generateToken(UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(sessionUserRepository).deleteByUsername(any());

    ResultActions result = mockMvc.perform(put("/api/auth/logout")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isOk());
  }
}
