package com.epam.gymapp.controller.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.UnauthorizedException;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.service.LoggingService;
import com.epam.gymapp.service.TrainerService;
import com.epam.gymapp.test.utils.JwtUtil;
import com.epam.gymapp.test.utils.TrainerTestUtil;
import com.epam.gymapp.test.utils.TrainingTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = TrainerControllerImpl.class)
public class TrainerControllerImplTest {

  @MockBean
  private TrainerService trainerService;

  @MockBean
  private JwtProcess jwtProcess;

  @SpyBean
  private LoggingService loggingService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createTrainer_Success() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    UserCredentialsDto expectedResult = UserTestUtil.getTrainerUserCredentialsDto1();

    // When
    when(trainerService.createTrainer(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(post("/api/trainers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

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
  void createTrainer_FirstNameIsNull_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_FirstNameIsEmpty_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName("");

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_FirstNameIsBlank_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_LastNameIsNull_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_LastNameIsEmpty_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName("");

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_LastNameIsBlank_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setLastName("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_SpecializationIsNull_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_SpecializationIsEmpty_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization("");

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_SpecializationIsBlank_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setSpecialization("    ");

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();
    trainerCreateDto.setFirstName(null);
    trainerCreateDto.setLastName(null);
    trainerCreateDto.setSpecialization(null);

    // When
    ResultActions result = mockMvc.perform(post("/api/trainers")
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
  void createTrainer_IfException_Failure() throws Exception {
    // Given
    TrainerCreateDto trainerCreateDto = TrainerTestUtil.getTrainerCreateDto1();

    // When
    when(trainerService.createTrainer(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(post("/api/trainers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainerCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void selectTrainers_Success() throws Exception {
    // Given
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    List<TrainerInfoDto> expectedResult = TrainerTestUtil.getTrainerInfoDtos();

    // When
    doNothing().when(jwtProcess).processToken(any());
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
  void selectTrainers_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/api/trainers")
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
  void selectTrainers_IfException_Failure() throws Exception {
    // Given
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    TrainerInfoDto expectedResult = TrainerTestUtil.getTrainerInfoDto1();

    // When
    doNothing().when(jwtProcess).processToken(any());
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
  void selectTrainer_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/api/trainers/" + trainerUsername)
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
  void selectTrainer_TraineeNotFound_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_1;
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    TrainerInfoDto expectedResult = TrainerTestUtil.getTrainerInfoDto1AfterUpdate();

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
  void updateTrainer_UnauthorizedAccess_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

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
            jsonPath("$[0].message").value("Unauthorized access")
        );
  }

  @Test
  void updateTrainer_TrainerNotFound_Failure() throws Exception {
    // Given
    TrainerUpdateDto trainerUpdateDto = TrainerTestUtil.getTrainerUpdateDto1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    List<TrainerShortInfoDto> expectedResult = TrainerTestUtil.getUnassignedTrainerShortInfoDtosOnTrainee1();

    // When
    doNothing().when(jwtProcess).processToken(any());
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
  void getUnassignedTrainersOnTrainee_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/api/trainers/unassigned/" + traineeUsername)
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
  void getUnassignedTrainersOnTrainee_IfException_Failure() throws Exception {
    // Given
    String traineeUsername = UserTestUtil.TEST_TRAINEE_USER_USERNAME_1;
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    List<TrainingInfoDto> expectedResult = List.of(TrainingTestUtil.getTrainingInfoDto1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());

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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());

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
  void getTrainerTrainings_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String trainerUsername = UserTestUtil.TEST_TRAINER_USER_USERNAME_2;
    String fromDate = "2024-02-05";
    String toDate = "2024-02-20";
    String traineeName = String.format("%s %s",
        UserTestUtil.TEST_TRAINEE_USER_FIRST_NAME_1, UserTestUtil.TEST_TRAINEE_USER_LAST_NAME_1);
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

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
            jsonPath("$[0].message").value("Unauthorized access")
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
}
