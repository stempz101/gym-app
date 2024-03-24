package com.epam.gymapp.service;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.JwtDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.UserNotFoundException;
import com.epam.gymapp.model.JwtToken;
import com.epam.gymapp.model.User;
import com.epam.gymapp.repository.JwtTokenRepository;
import com.epam.gymapp.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private static final String BAD_CREDENTIALS_MESSAGE = "Specified wrong username or password";

  private final UserRepository userRepository;
  private final JwtTokenRepository jwtTokenRepository;
  private final JwtService jwtService;

  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public JwtDto authenticate(UserCredentialsDto userCredentialsDto) {
    log.info("Authenticating User: {}", userCredentialsDto.getUsername());

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            userCredentialsDto.getUsername(), String.valueOf(userCredentialsDto.getPassword()))
    );

    User user = (User) authentication.getPrincipal();
    String token = jwtService.generateToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, token);

    log.info("User (username={}) authenticated successfully", userCredentialsDto.getUsername());
    return new JwtDto(token);
  }

  @Transactional
  public void changePassword(ChangePasswordDto changePasswordDto) {
    log.info("Changing User's (username={}) password", changePasswordDto.getUsername());

    User user = checkCredentials(changePasswordDto.getUsername(), changePasswordDto.getOldPassword());
    user.setPassword(passwordEncoder.encode(String.valueOf(changePasswordDto.getNewPassword())));

    userRepository.save(user);

    log.info("User's (username={}) password was changed successfully",
        changePasswordDto.getUsername());
  }

  @Transactional
  public void changeActivationStatus(UserActivateDto userActivateDto) {
    if (userActivateDto.getIsActive()) {
      log.info("Activating User: {}", userActivateDto.getUsername());
    } else {
      log.info("Deactivating User: {}", userActivateDto.getUsername());
    }

    User user = userRepository.findByUsernameIgnoreCase(userActivateDto.getUsername())
        .orElseThrow(() -> new UserNotFoundException(userActivateDto.getUsername()));

    user.setActive(userActivateDto.getIsActive());
    user = userRepository.save(user);

    if (user.isActive()) {
      log.info("User activated successfully: {}", userActivateDto.getUsername());
    } else {
      log.info("User deactivated successfully: {}", userActivateDto.getUsername());
    }
  }

  private User checkCredentials(String username, char[] password) {
    User user = userRepository.findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new BadCredentialsException(BAD_CREDENTIALS_MESSAGE));

    if (!passwordEncoder.matches(String.valueOf(password), user.getPassword())) {
      throw new BadCredentialsException(BAD_CREDENTIALS_MESSAGE);
    }

    return user;
  }

  private void revokeAllUserTokens(User user) {
    List<JwtToken> jwtTokens = jwtTokenRepository.findAllValidTokensByUserId(user.getId());

    if (jwtTokens.isEmpty()) {
      return;
    }
    jwtTokens.forEach(jwt -> jwt.setRevoked(true));
    jwtTokenRepository.saveAll(jwtTokens);
  }

  private void saveUserToken(User user, String token) {
    JwtToken jwtToken = JwtToken.builder()
        .token(token)
        .user(user)
        .build();
    jwtTokenRepository.save(jwtToken);
  }
}
