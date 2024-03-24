package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.UserController;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

  private static final Logger log = LoggerFactory.getLogger(UserControllerImpl.class);

  private final UserService userService;

  @Override
  public void changeActivationStatus(UserActivateDto userActivateDto) {
    log.info("Starting to change an activation status of the User (username={})",
        userActivateDto.getUsername());
    userService.changeActivationStatus(userActivateDto);
  }
}
