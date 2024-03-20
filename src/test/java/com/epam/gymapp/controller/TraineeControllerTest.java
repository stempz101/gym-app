package com.epam.gymapp.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.controller.error.ExceptionHandlerController;
import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.dto.TraineeInfoDto;
import com.epam.gymapp.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.dto.TraineeUpdateDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.UnauthorizedException;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.logging.LoggerHelper;
import com.epam.gymapp.service.TraineeService;
import com.epam.gymapp.test.utils.JwtUtil;
import com.epam.gymapp.test.utils.TraineeTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.TrainingTypeTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
public class TraineeControllerTest {

  @InjectMocks
  private TraineeController traineeController;

  @Mock
  private TraineeService traineeService;

  @Mock
  private JwtProcess jwtProcess;

  @Spy
  private LoggerHelper loggerHelper;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(traineeController)
        .setValidator(new LocalValidatorFactoryBean())
        .setControllerAdvice(new ExceptionHandlerController())
        .build();

    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  void createTrainee_Success() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    UserCredentialsDto expectedResult = UserTestUtil.getTraineeUserCredentialsDto1();

    // When
    when(traineeService.createTrainee(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(post("/trainees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isCreated(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$.username").value(expectedResult.getUsername()),
            jsonPath("$.password").value(String.valueOf(expectedResult.getPassword()))
        );
  }

  @Test
  void createTrainee_FirstNameIsNull_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName(null);

    // When
    ResultActions result = mockMvc.perform(post("/trainees")
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
  void createTrainee_FirstNameIsEmpty_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName("");

    // When
    ResultActions result = mockMvc.perform(post("/trainees")
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
  void createTrainee_FirstNameIsBlank_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName("    ");

    // When
    ResultActions result = mockMvc.perform(post("/trainees")
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
  void createTrainee_LastNameIsNull_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName(null);

    // When
    ResultActions result = mockMvc.perform(post("/trainees")
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
  void createTrainee_LastNameIsEmpty_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName("");

    // When
    ResultActions result = mockMvc.perform(post("/trainees")
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
  void createTrainee_LastNameIsBlank_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setLastName("    ");

    // When
    ResultActions result = mockMvc.perform(post("/trainees")
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
  void createTrainee_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();
    traineeCreateDto.setFirstName(null);
    traineeCreateDto.setLastName(null);

    // When
    ResultActions result = mockMvc.perform(post("/trainees")
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
  void createTrainee_IfException_Failure() throws Exception {
    // Given
    TraineeCreateDto traineeCreateDto = TraineeTestUtil.getTraineeCreateDto1();

    // When
    when(traineeService.createTrainee(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(post("/trainees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(traineeCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void selectTrainees_Success() throws Exception {
    // Given
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    List<TraineeInfoDto> expectedResult = TraineeTestUtil.getTraineeInfoDtos();

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.selectTrainees()).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/trainees")
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
  void selectTrainees_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/trainees")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isUnauthorized(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Unauthorized access")
        );
  }

  @Test
  void selectTrainees_IfException_Failure() throws Exception {
    // Given
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.selectTrainees()).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1();

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.selectTrainee(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/trainees/" + traineeUsername)
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
  void selectTrainee_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isUnauthorized(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Unauthorized access")
        );
  }

  @Test
  void selectTrainee_TraineeNotFound_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.selectTrainee(any())).thenThrow(new TraineeNotFoundException(traineeUsername));

    ResultActions result = mockMvc.perform(get("/trainees/" + traineeUsername)
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.selectTrainee(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/trainees/" + traineeUsername)
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    TraineeInfoDto expectedResult = TraineeTestUtil.getTraineeInfoDto1AfterUpdate();

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.updateTrainee(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
            jsonPath("$[0].message").value("Active status must be specified")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees")
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
                    "Active status must be specified"
                ))
        );
  }

  @Test
  void updateTrainee_UnauthorizedAccess_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(put("/trainees")
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
            jsonPath("$[0].message").value("Unauthorized access")
        );
  }

  @Test
  void updateTrainee_TraineeNotFound_Failure() throws Exception {
    // Given
    TraineeUpdateDto traineeUpdateDto = TraineeTestUtil.getTraineeUpdateDto1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.updateTrainee(any()))
        .thenThrow(new TraineeNotFoundException(traineeUpdateDto.getUsername()));

    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.updateTrainee(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(put("/trainees")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    doNothing().when(traineeService).deleteTrainee(any());

    ResultActions result = mockMvc.perform(delete("/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(status().isOk());
  }

  @Test
  void deleteTrainee_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(delete("/trainees/" + traineeUsername)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isUnauthorized(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Unauthorized access")
        );
  }

  @Test
  void deleteTrainee_TraineeNotFound_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    doThrow(new TraineeNotFoundException(traineeUsername)).when(traineeService).deleteTrainee(any());

    ResultActions result = mockMvc.perform(delete("/trainees/" + traineeUsername)
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
  void deleteTrainee_IfException_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    doThrow(RuntimeException.class).when(traineeService).deleteTrainee(any());

    ResultActions result = mockMvc.perform(delete("/trainees/" + traineeUsername)
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    List<TrainerShortInfoDto> expectedResult = TraineeTestUtil.getTraineeUpdatedTrainerShortInfoDtosList2();

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.updateTrainerList(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
  void updateTrainerListOfTrainee_UnauthorizedAccess_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
            jsonPath("$[0].message").value("Unauthorized access")
        );
  }

  @Test
  void updateTrainerListOfTrainee_TraineeNotFound_Failure() throws Exception {
    // Given
    TraineeTrainersUpdateDto traineeTrainersUpdateDto = TraineeTestUtil.getTraineeTrainersUpdateDto2();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.updateTrainerList(any()))
        .thenThrow(new TraineeNotFoundException(traineeTrainersUpdateDto.getTraineeUsername()));

    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.updateTrainerList(any()))
        .thenThrow(new TrainerNotFoundException(traineeTrainersUpdateDto.getTrainerUsernames()));

    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.updateTrainerList(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(put("/trainees/trainers")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    List<TrainingInfoDto> expectedResult = List.of(TrainingTestUtil.getTrainingInfoDto2());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.findTraineeTrainings(any(), any(), any(), any(), any()))
        .thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/trainees/trainings")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(get("/trainees/trainings")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/trainees/trainings")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/trainees/trainings")
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
  void getTraineeTrainings_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String trainerName = String.format("%s %s",
        UserTestUtil.TEST_TRAINER_USER_FIRST_NAME_3, UserTestUtil.TEST_TRAINER_USER_LAST_NAME_3);
    String trainingType = TrainingTypeTestUtil.TEST_TRAINING_TYPE_NAME_3;
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/trainees/trainings")
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
            jsonPath("$[0].message").value("Unauthorized access")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(traineeService.findTraineeTrainings(any(), any(), any(), any(), any()))
        .thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/trainees/trainings")
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
