package com.epam.gymapp.controller.impl;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.exception.UnauthorizedException;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.service.LoggingService;
import com.epam.gymapp.service.TrainingTypeService;
import com.epam.gymapp.test.utils.JwtUtil;
import com.epam.gymapp.test.utils.TrainingTypeTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
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
@WebMvcTest(value = TrainingTypeControllerImpl.class)
public class TrainingTypeControllerImplTest {

  @MockBean
  private TrainingTypeService trainingTypeService;

  @MockBean
  private JwtProcess jwtProcess;

  @SpyBean
  private LoggingService loggingService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void selectTrainingTypes_Success() throws Exception {
    // Given
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    List<TrainingType> expectedResult = TrainingTypeTestUtil.getTrainingTypes();

    // When
    doNothing().when(jwtProcess).processToken(any());
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
  void selectTrainingTypes_UnauthorizedAccess_Failure() throws Exception {
    // Given
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

    ResultActions result = mockMvc.perform(get("/api/training-types")
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
  void selectTrainingTypes_IfException_Failure() throws Exception {
    // Given
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
    when(trainingTypeService.selectTrainingTypes()).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(get("/api/training-types")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
