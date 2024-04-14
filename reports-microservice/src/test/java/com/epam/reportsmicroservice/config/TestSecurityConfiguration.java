package com.epam.reportsmicroservice.config;

import com.epam.reportsmicroservice.config.sercurity.CustomAuthenticationEntryPoint;
import com.epam.reportsmicroservice.config.sercurity.SecurityConfiguration;
import com.epam.reportsmicroservice.service.JwtService;
import com.epam.reportsmicroservice.test.utils.JwtTokenTestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

@TestConfiguration
@Import(SecurityConfiguration.class)
@PropertySource(value = "classpath:application.properties")
public class TestSecurityConfiguration {

  @Value("${application.security.token.expiration-period}")
  private int expirationPeriod;

  @Bean
  public JwtService jwtService() {
    return new JwtService();
  }

  @Bean
  public JwtTokenTestUtil jwtTokenTestUtil() {
    return new JwtTokenTestUtil(expirationPeriod);
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(
      HandlerExceptionResolver handlerExceptionResolver) {
    return new CustomAuthenticationEntryPoint(handlerExceptionResolver);
  }
}
