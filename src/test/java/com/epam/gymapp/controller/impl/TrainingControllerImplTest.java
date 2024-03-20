package com.epam.gymapp.controller.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.exception.TraineeNotFoundException;
import com.epam.gymapp.exception.TrainerNotFoundException;
import com.epam.gymapp.exception.UnauthorizedException;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.service.LoggingService;
import com.epam.gymapp.service.TrainingService;
import com.epam.gymapp.test.utils.JwtUtil;
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
@WebMvcTest(value = TrainingControllerImpl.class)
public class TrainingControllerImplTest {

  @MockBean
  private TrainingService trainingService;

  @MockBean
  private JwtProcess jwtProcess;

  @SpyBean
  private LoggingService loggingService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void addTraining_Success() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    doNothing().when(trainingService).addTraining(any());

    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void addTraining_TraineeUsernameIsNull_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTraineeUsername(null);
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TraineeUsernameIsEmpty_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTraineeUsername("");
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TraineeUsernameIsBlank_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTraineeUsername("    ");
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TrainerUsernameIsNull_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTrainerUsername(null);
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TrainerUsernameIsEmpty_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTrainerUsername("");
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TrainerUsernameIsBlank_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTrainerUsername("    ");
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TrainingNameIsNull_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName(null);
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Training name must be specified")
        );
  }

  @Test
  void addTraining_TrainingNameIsEmpty_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName("");
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Training name must be specified")
        );
  }

  @Test
  void addTraining_TrainingNameIsBlank_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setName("    ");
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message").value("Training name must be specified")
        );
  }

  @Test
  void addTraining_TrainingDateIsNull_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setDate(null);
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TrainingDurationIsNull_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setDuration(null);
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TrainingDurationIsNegative_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setDuration(-1);
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    trainingCreateDto.setTraineeUsername(null);
    trainingCreateDto.setTrainerUsername(null);
    trainingCreateDto.setName(null);
    trainingCreateDto.setDate(null);
    trainingCreateDto.setDuration(null);
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isBadRequest(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(5)),
            jsonPath("$[*].message")
                .value(containsInAnyOrder(
                    "Trainee's username must be specified",
                    "Trainer's username must be specified",
                    "Training name must be specified",
                    "Training date must be specified",
                    "Training duration must be specified"
                ))
        );
  }

  @Test
  void addTraining_UnauthorizedAccess_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

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
  void addTraining_TraineeNotFound_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    doThrow(new TraineeNotFoundException(trainingCreateDto.getTraineeUsername()))
        .when(trainingService).addTraining(any());

    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainee with username '%s' is not found",
                    trainingCreateDto.getTraineeUsername()))
        );
  }

  @Test
  void addTraining_TrainerNotFound_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    doThrow(new TrainerNotFoundException(trainingCreateDto.getTrainerUsername()))
        .when(trainingService).addTraining(any());

    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("Trainer with username '%s' is not found",
                    trainingCreateDto.getTrainerUsername()))
        );
  }

  @Test
  void addTraining_IfException_Failure() throws Exception {
    // Given
    TrainingCreateDto trainingCreateDto = TrainingTestUtil.getTrainingCreateDto1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    doThrow(RuntimeException.class).when(trainingService).addTraining(any());

    ResultActions result = mockMvc.perform(post("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trainingCreateDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void selectTrainings_Success() throws Exception {
    // Given
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    List<TrainingInfoDto> expectedResult = TrainingTestUtil.getTrainingInfoDtos();

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(trainingService.selectTrainings()).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(get("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isOk(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(2)),

            // Item #1
            jsonPath("$[0].name").value(expectedResult.get(0).getName()),
            jsonPath("$[0].date").value(expectedResult.get(0).getDate()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)),
            jsonPath("$[0].type").value(expectedResult.get(0).getType()),
            jsonPath("$[0].duration").value(expectedResult.get(0).getDuration()),
            jsonPath("$[0].trainerName").value(expectedResult.get(0).getTrainerName()),
            jsonPath("$[0].traineeName").value(expectedResult.get(0).getTraineeName()),

            // Item #2
            jsonPath("$[1].name").value(expectedResult.get(1).getName()),
            jsonPath("$[1].date").value(expectedResult.get(1).getDate()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)),
            jsonPath("$[1].type").value(expectedResult.get(1).getType()),
            jsonPath("$[1].duration").value(expectedResult.get(1).getDuration()),
            jsonPath("$[1].trainerName").value(expectedResult.get(1).getTrainerName()),
            jsonPath("$[1].traineeName").value(expectedResult.get(1).getTraineeName())
        );
  }

  @Test
  void selectTrainings_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/api/trainings")
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
  void selectTrainings_IfException_Failure() throws Exception {
    // Given
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(trainingService.selectTrainings()).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/trainings")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
