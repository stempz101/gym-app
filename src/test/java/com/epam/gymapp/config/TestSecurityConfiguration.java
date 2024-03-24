package com.epam.gymapp.config;

import com.epam.gymapp.config.security.CustomAuthenticationEntryPoint;
import com.epam.gymapp.config.security.SecurityBeansConfiguration;
import com.epam.gymapp.config.security.SecurityConfiguration;
import com.epam.gymapp.listener.AuthenticationFailureListener;
import com.epam.gymapp.repository.JwtTokenRepository;
import com.epam.gymapp.service.JwtService;
import com.epam.gymapp.service.LoggingService;
import com.epam.gymapp.service.LoginAttemptService;
import com.epam.gymapp.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

@TestConfiguration
@Import({SecurityBeansConfiguration.class, SecurityConfiguration.class})
public class TestSecurityConfiguration {

  @Bean
  public JwtService jwtService(JwtTokenRepository jwtTokenRepository) {
    return new JwtService(jwtTokenRepository);
  }

  @Bean
  public LoginAttemptService loginAttemptService(HttpServletRequest httpServletRequest) {
    return new LoginAttemptService(httpServletRequest);
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(
      HandlerExceptionResolver handlerExceptionResolver) {
    return new CustomAuthenticationEntryPoint(handlerExceptionResolver);
  }

  @Bean
  public LogoutHandler logoutHandler(LoggingService loggingService, JwtService jwtService,
      JwtTokenRepository jwtTokenRepository) {
    return new LogoutService(loggingService, jwtService, jwtTokenRepository);
  }

  @Bean
  public AuthenticationFailureListener authenticationFailureListener(
      HttpServletRequest httpServletRequest, LoginAttemptService loginAttemptService) {
    return new AuthenticationFailureListener(httpServletRequest, loginAttemptService);
  }
}
