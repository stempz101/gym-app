package com.epam.gymapp.mainmicroservice.config;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;

@TestConfiguration
public class TestJmsConfiguration {

  private static final GenericContainer<?> ACTIVE_MQ = new GenericContainer<>("rmohr/activemq:latest")
      .withExposedPorts(61616);

  static  {
    ACTIVE_MQ.start();
    System.setProperty("spring.activemq.broker-url", "tcp://localhost:" + ACTIVE_MQ.getMappedPort(61616));
  }

  @PreDestroy
  public void shutdown() {
    ACTIVE_MQ.stop();
  }
}
