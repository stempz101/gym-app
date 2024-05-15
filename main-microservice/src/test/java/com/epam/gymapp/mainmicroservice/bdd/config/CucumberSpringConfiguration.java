package com.epam.gymapp.mainmicroservice.bdd.config;

import jakarta.annotation.PreDestroy;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class CucumberSpringConfiguration {

  protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest")
      .withExposedPorts(5432)
      .withEnv("POSTGRES_USER", "user")
      .withEnv("POSTGRES_PASSWORD", "pass")
      .withEnv("POSTGRES_DB", "gym_db");

  protected static final GenericContainer<?> REDIS = new GenericContainer<>("redis:latest")
      .withExposedPorts(6379);

  protected static final GenericContainer<?> ACTIVE_MQ = new GenericContainer<>("rmohr/activemq:latest")
      .withExposedPorts(61616);

  static {
    POSTGRES.start();
    REDIS.start();
    ACTIVE_MQ.start();
  }

  @DynamicPropertySource
  public static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    registry.add("spring.data.redis.host", REDIS::getHost);
    registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));
    registry.add("spring.activemq.broker-url",
        () -> "tcp://localhost:" + ACTIVE_MQ.getMappedPort(61616));
  }

  @PreDestroy
  public void shutdown() {
    POSTGRES.stop();
    REDIS.stop();
    ACTIVE_MQ.stop();
  }
}
