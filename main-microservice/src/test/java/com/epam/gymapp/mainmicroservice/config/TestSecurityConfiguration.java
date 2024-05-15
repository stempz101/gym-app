package com.epam.gymapp.mainmicroservice.config;

import com.epam.gymapp.mainmicroservice.config.security.CustomAuthenticationEntryPoint;
import com.epam.gymapp.mainmicroservice.config.security.SecurityBeansConfiguration;
import com.epam.gymapp.mainmicroservice.config.security.SecurityConfiguration;
import com.epam.gymapp.mainmicroservice.listener.AuthenticationFailureListener;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.service.JwtService;
import com.epam.gymapp.mainmicroservice.service.LoggingService;
import com.epam.gymapp.mainmicroservice.service.LoginAttemptService;
import com.epam.gymapp.mainmicroservice.service.LogoutService;
import com.epam.gymapp.mainmicroservice.test.utils.JwtTokenTestUtil;
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
  public JwtService jwtService(SessionUserRepository sessionUserRepository) {
    return new JwtService(sessionUserRepository);
  }

  @Bean
  public JwtTokenTestUtil jwtTokenTestUtil(SessionUserRepository sessionUserRepository) {
    return new JwtTokenTestUtil(sessionUserRepository);
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
      SessionUserRepository sessionUserRepository) {
    return new LogoutService(loggingService, jwtService, sessionUserRepository);
  }

  @Bean
  public AuthenticationFailureListener authenticationFailureListener(
      HttpServletRequest httpServletRequest, LoginAttemptService loginAttemptService) {
    return new AuthenticationFailureListener(httpServletRequest, loginAttemptService);
  }
}
