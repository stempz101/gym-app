package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.UserController;
import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.JwtDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

  private final UserService userService;
  private final JwtProcess jwtProcess;

  @Override
  public JwtDto authenticate(UserCredentialsDto userCredentialsDto, HttpServletRequest request) {
    return userService.authenticate(userCredentialsDto);
  }

  @Override
  public void changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) {
    userService.changePassword(changePasswordDto);
  }

  @Override
  public void changeActivationStatus(UserActivateDto userActivateDto, HttpServletRequest request) {
    jwtProcess.processToken(request);
    userService.changeActivationStatus(userActivateDto);
  }
}
