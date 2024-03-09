package com.epam.gymapp.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.JwtDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.BadCredentialsException;
import com.epam.gymapp.exception.UserNotFoundException;
import com.epam.gymapp.jwt.JwtService;
import com.epam.gymapp.model.User;
import com.epam.gymapp.repository.UserRepository;
import com.epam.gymapp.test.utils.UserTestUtil;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private JwtService jwtService;

  @Test
  void authenticate_Success() {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    User user = UserTestUtil.getTraineeUser1();
    String token = UserTestUtil.getUserToken();
    JwtDto expectedResult = UserTestUtil.getUserJwtDto(token);

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
    when(jwtService.generateToken(any())).thenReturn(token);

    JwtDto result = userService.authenticate(userCredentialsDto);

    verify(userRepository, times(1)).findByUsername(any());
    verify(jwtService, times(1)).generateToken(any());

    assertThat(result, samePropertyValuesAs(expectedResult));
  }

  @Test
  void authenticate_UserNotFound_Failure() {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(BadCredentialsException.class, () -> userService.authenticate(userCredentialsDto));
  }

  @Test
  void authenticate_PasswordNotEqual_Failure() {
    // Given
    UserCredentialsDto userCredentialsDto = UserTestUtil.getTraineeUserCredentialsDto1();
    userCredentialsDto.setPassword("uTlAHAgZg2".toCharArray());
    User user = UserTestUtil.getTraineeUser1();

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

    // Then
    assertThrows(BadCredentialsException.class, () -> userService.authenticate(userCredentialsDto));
  }

  @Test
  void changePassword_Success() {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    User oldUser = UserTestUtil.getTraineeUser1();
    User updatedUser = UserTestUtil.getTraineeUser1();
    updatedUser.setPassword(UserTestUtil.TEST_TRAINEE_USER_NEW_PASSWORD_1);

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(oldUser));
    when(userRepository.update(any())).thenReturn(updatedUser);

    userService.changePassword(changePasswordDto);

    // Then
    verify(userRepository, times(1)).findByUsername(any());
    verify(userRepository, times(1)).update(any());
  }

  @Test
  void changePassword_UserNotFound_Failure() {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(BadCredentialsException.class,
        () -> userService.changePassword(changePasswordDto));
  }

  @Test
  void changePassword_OldPasswordNotEqualToTheCurrentPassword_Failure() {
    // Given
    ChangePasswordDto changePasswordDto = UserTestUtil.getChangePasswordDto1();
    changePasswordDto.setOldPassword("uTlAHAgZg2".toCharArray());
    User oldUser = UserTestUtil.getTraineeUser1();

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(oldUser));

    // Then
    assertThrows(BadCredentialsException.class,
        () -> userService.changePassword(changePasswordDto));
  }

  @Test
  void changeActivationStatus_Activate_Success() {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();
    User oldUser = UserTestUtil.getTraineeUser1();
    User updatedUser = UserTestUtil.getTraineeUser1();
    updatedUser.setActive(true);

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(oldUser));
    when(userRepository.update(any())).thenReturn(updatedUser);

    userService.changeActivationStatus(userActivation);

    // Then
    verify(userRepository, times(1)).findByUsername(any());
    verify(userRepository, times(1)).update(any());
  }

  @Test
  void changeActivationStatus_Deactivate_Success() {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserDeactivation1();
    User oldUser = UserTestUtil.getTraineeUser1();
    User updatedUser = UserTestUtil.getTraineeUser1();
    updatedUser.setActive(false);

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.of(oldUser));
    when(userRepository.update(any())).thenReturn(updatedUser);

    userService.changeActivationStatus(userActivation);

    // Then
    verify(userRepository, times(1)).findByUsername(any());
    verify(userRepository, times(1)).update(any());
  }

  @Test
  void changeActivationStatus_UserNotFound_Failure() {
    // Given
    UserActivateDto userActivation = UserTestUtil.getUserActivation1();

    // When
    when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

    // Then
    assertThrows(UserNotFoundException.class,
        () -> userService.changeActivationStatus(userActivation));
  }
}
