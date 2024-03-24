package com.epam.gymapp.controller.impl;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.config.TestSecurityConfiguration;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.exception.UserNotFoundException;
import com.epam.gymapp.model.JwtToken;
import com.epam.gymapp.repository.JwtTokenRepository;
import com.epam.gymapp.repository.UserRepository;
import com.epam.gymapp.service.LoggingService;
import com.epam.gymapp.service.UserService;
import com.epam.gymapp.test.utils.JwtTokenTestUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
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
@WebMvcTest(value = UserControllerImpl.class)
@ContextConfiguration(classes = TestSecurityConfiguration.class)
public class UserControllerImplTest {

  @MockBean
  private UserService userService;

  @SpyBean
  private LoggingService loggingService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private JwtTokenRepository jwtTokenRepository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void changeActivationStatus_Success() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    JwtToken jwt = JwtTokenTestUtil.getNewJwtTokenOfTrainee1();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(jwt.getUser()));
    doNothing().when(userService).changeActivationStatus(any());

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void changeActivationStatus_UsernameIsNull_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    userActivation.setUsername(null);
    JwtToken jwt = JwtTokenTestUtil.getNewJwtTokenOfTrainee1();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(jwt.getUser()));

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

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
  void changeActivationStatus_UsernameIsEmpty_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    userActivation.setUsername("");
    JwtToken jwt = JwtTokenTestUtil.getNewJwtTokenOfTrainee1();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(jwt.getUser()));

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

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
  void changeActivationStatus_UsernameIsBlank_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    userActivation.setUsername("    ");
    JwtToken jwt = JwtTokenTestUtil.getNewJwtTokenOfTrainee1();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(jwt.getUser()));

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

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
  void changeActivationStatus_ActiveIsNull_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    userActivation.setIsActive(null);
    JwtToken jwt = JwtTokenTestUtil.getNewJwtTokenOfTrainee1();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(jwt.getUser()));

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

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
  void changeActivationStatus_NoJwtToken_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();

    // When
    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

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
  void changeActivationStatus_JwtTokenExpired_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    String token = JwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

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
  void changeActivationStatus_UserNotFound_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    JwtToken jwt = JwtTokenTestUtil.getNewJwtTokenOfTrainee1();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(jwt.getUser()));
    doThrow(new UserNotFoundException(userActivation.getUsername()))
        .when(userService).changeActivationStatus(any());

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

    // Then
    result
        .andDo(print())
        .andExpectAll(
            status().isNotFound(),
            content().contentType(MediaType.APPLICATION_JSON),
            jsonPath("$").value(hasSize(1)),
            jsonPath("$[0].message")
                .value(String.format("User with username '%s' is not found",
                    userActivation.getUsername()))
        );
  }

  @Test
  void changeActivationStatus_IfException_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    JwtToken jwt = JwtTokenTestUtil.getNewJwtTokenOfTrainee1();

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(jwt.getUser()));
    doThrow(RuntimeException.class).when(userService).changeActivationStatus(any());

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
