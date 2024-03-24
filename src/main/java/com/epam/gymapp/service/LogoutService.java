package com.epam.gymapp.service;

import com.epam.gymapp.repository.JwtTokenRepository;
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
  private final JwtTokenRepository jwtTokenRepository;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    loggingService.logRequest(request, null);

    jwtService.extractBearerToken(request)
        .flatMap(jwtTokenRepository::findByToken)
        .ifPresent(token -> {
          token.setRevoked(true);
          jwtTokenRepository.save(token);
          log.info("User logged out successfully");
        });
  }
}
