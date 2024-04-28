package com.epam.gymapp.mainmicroservice.controller.impl;

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

import com.epam.gymapp.mainmicroservice.config.TestSecurityConfiguration;
import com.epam.gymapp.mainmicroservice.dto.UserActivateDto;
import com.epam.gymapp.mainmicroservice.exception.UserNotFoundException;
import com.epam.gymapp.mainmicroservice.model.SessionUser;
import com.epam.gymapp.mainmicroservice.model.User;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.repository.UserRepository;
import com.epam.gymapp.mainmicroservice.service.LoggingService;
import com.epam.gymapp.mainmicroservice.service.UserService;
import com.epam.gymapp.mainmicroservice.test.utils.JwtTokenTestUtil;
import com.epam.gymapp.mainmicroservice.test.utils.UserTestUtil;
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
  private SessionUserRepository sessionUserRepository;

  @Autowired
  private JwtTokenTestUtil jwtTokenTestUtil;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void changeActivationStatus_Success() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    doNothing().when(userService).changeActivationStatus(any());

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
  void changeActivationStatus_JwtTokenRevoked_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);

    // When
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    when(sessionUserRepository.findById(any())).thenReturn(Optional.empty());

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
            jsonPath("$[0].message").value("Full authentication is required to access this resource")
        );
  }

  @Test
  void changeActivationStatus_JwtTokenExpired_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    String token = jwtTokenTestUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

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
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    doThrow(new UserNotFoundException(userActivation.getUsername()))
        .when(userService).changeActivationStatus(any());

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
    User user = UserTestUtil.getTraineeUser1();
    String token = jwtTokenTestUtil.generateToken(user);
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());

    // When
    when(sessionUserRepository.findById(any())).thenReturn(Optional.of(sessionUser));
    when(userRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.of(user));
    doThrow(RuntimeException.class).when(userService).changeActivationStatus(any());

    ResultActions result = mockMvc.perform(patch("/api/users/change-activation-status")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userActivation)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }
}
