package com.epam.gymapp.service;

import com.epam.gymapp.repository.SessionUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private static final Logger log = LoggerFactory.getLogger(LogoutService.class);

  private final LoggingService loggingService;

  private final JwtService jwtService;
  private final SessionUserRepository sessionUserRepository;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    loggingService.logRequest(request, null);

    jwtService.extractBearerToken(request)
        .ifPresent(token -> {
          String username = jwtService.extractUsername(token);
          sessionUserRepository.deleteByUsername(username);
          log.info("User logged out successfully");
        });
  }
}
