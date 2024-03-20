package com.epam.gymapp.controller.impl;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.JwtDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.BadCredentialsException;
import com.epam.gymapp.exception.UnauthorizedException;
import com.epam.gymapp.exception.UserNotFoundException;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.service.LoggingService;
import com.epam.gymapp.service.UserService;
import com.epam.gymapp.test.utils.JwtUtil;
import com.epam.gymapp.test.utils.UserTestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
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
@WebMvcTest(value = UserControllerImpl.class)
public class UserControllerImplTest {

  @MockBean
  private UserService userService;

  @MockBean
  private JwtProcess jwtProcess;

  @SpyBean
  private LoggingService loggingService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void authenticate_Success() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());
    JwtDto expectedResult = UserTestUtil.getUserJwtDto(token);

    // When
    when(userService.authenticate(any())).thenReturn(expectedResult);

    ResultActions result = mockMvc.perform(post("/api/users/authenticate")
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
    ResultActions result = mockMvc.perform(post("/api/users/authenticate")
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
    ResultActions result = mockMvc.perform(post("/api/users/authenticate")
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
    ResultActions result = mockMvc.perform(post("/api/users/authenticate")
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
    ResultActions result = mockMvc.perform(post("/api/users/authenticate")
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
    ResultActions result = mockMvc.perform(post("/api/users/authenticate")
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
    when(userService.authenticate(any())).thenThrow(new BadCredentialsException());

    ResultActions result = mockMvc.perform(post("/api/users/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

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
  void authenticate_IfException_Failure() throws Exception {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();

    // When
    when(userService.authenticate(any())).thenThrow(RuntimeException.class);

    ResultActions result = mockMvc.perform(post("/api/users/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentialsDto)));

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

    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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
    doThrow(new BadCredentialsException()).when(userService).changePassword(any());

    ResultActions result = mockMvc.perform(put("/api/users/change-password")
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

    ResultActions result = mockMvc.perform(put("/api/users/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(changePasswordDto)));

    // Then
    result
        .andDo(print())
        .andExpect(status().isInternalServerError());
  }

  @Test
  void changeActivationStatus_Success() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
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
  void changeActivationStatus_UnauthorizedAccess_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    String token = JwtUtil.generateExpiredToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doThrow(new UnauthorizedException()).when(jwtProcess).processToken(any());

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
            jsonPath("$[0].message").value("Unauthorized access")
        );
  }

  @Test
  void changeActivationStatus_UserNotFound_Failure() throws Exception {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
    String token = JwtUtil.generateToken(new HashMap<>(), UserTestUtil.getTraineeUser1());

    // When
    doNothing().when(jwtProcess).processToken(any());
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
