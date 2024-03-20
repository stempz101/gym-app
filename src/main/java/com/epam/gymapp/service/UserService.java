package com.epam.gymapp.service;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.JwtDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.BadCredentialsException;
import com.epam.gymapp.exception.UserNotFoundException;
import com.epam.gymapp.jwt.JwtService;
import com.epam.gymapp.model.User;
import com.epam.gymapp.repository.UserRepository;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final JwtService jwtService;

  public JwtDto authenticate(UserCredentialsDto userCredentialsDto) {
    log.info("Authenticating User: {}", userCredentialsDto.getUsername());

    User user = checkCredentials(userCredentialsDto.getUsername(), userCredentialsDto.getPassword());
    String token = jwtService.generateToken(user);

    log.info("User (username={}) authenticated successfully", userCredentialsDto.getUsername());
    return new JwtDto(token);
  }

  public void changePassword(ChangePasswordDto changePasswordDto) {
    log.info("Changing User's (username={}) password", changePasswordDto.getUsername());

    User user = checkCredentials(changePasswordDto.getUsername(), changePasswordDto.getOldPassword());
    user.setPassword(changePasswordDto.getNewPassword());

    userRepository.update(user);

    log.info("User's (username={}) password was changed successfully",
        changePasswordDto.getUsername());
  }

  public void changeActivationStatus(UserActivateDto userActivateDto) {
    if (userActivateDto.getIsActive()) {
      log.info("Activating User: {}", userActivateDto.getUsername());
    } else {
      log.info("Deactivating User: {}", userActivateDto.getUsername());
    }

    User user = userRepository.findByUsername(userActivateDto.getUsername())
        .orElseThrow(() -> new UserNotFoundException(userActivateDto.getUsername()));

    user.setActive(userActivateDto.getIsActive());
    user = userRepository.update(user);

    if (user.isActive()) {
      log.info("User activated successfully: {}", userActivateDto.getUsername());
    } else {
      log.info("User deactivated successfully: {}", userActivateDto.getUsername());
    }
  }

  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  private User checkCredentials(String username, char[] password) {
    User user = userRepository.findByUsername(username).orElseThrow(BadCredentialsException::new);

    if (!Arrays.equals(password, user.getPassword())) {
      throw new BadCredentialsException();
    }

    return user;
  }
}
