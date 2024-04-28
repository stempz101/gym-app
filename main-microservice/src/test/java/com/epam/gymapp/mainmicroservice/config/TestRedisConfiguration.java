package com.epam.gymapp.mainmicroservice.config;

import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.test.utils.JwtTokenTestUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import redis.embedded.RedisServer;

@TestConfiguration
@ComponentScan(basePackages = "com.epam.gymapp.mainmicroservice.repository.redis")
@Import(RedisConfiguration.class)
@PropertySource(value = "classpath:application.properties")
public class TestRedisConfiguration {

  private RedisServer redisServer;

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private String redisPort;

  @PostConstruct
  public void postConstruct() throws IOException {
    redisServer = new RedisServer(Integer.parseInt(redisPort));
    redisServer.start();
  }

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(
        new RedisStandaloneConfiguration(redisHost, Integer.parseInt(redisPort)));
  }

  @Bean
  public JwtTokenTestUtil jwtTokenTestUtil(SessionUserRepository sessionUserRepository) {
    return new JwtTokenTestUtil(sessionUserRepository);
  }

  @PreDestroy
  public void preDestroy() throws IOException {
    if (redisServer != null) {
      redisServer.stop();
    }
  }
}
