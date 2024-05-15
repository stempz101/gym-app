package com.epam.gymapp.reportsmicroservice.config;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration
@ComponentScan(basePackages = "com.epam.gymapp.reportsmicroservice.repository")
public class TestMongoConfiguration {

  private static final MongoDBContainer MONGO = new MongoDBContainer("mongo:latest")
      .withExposedPorts(27017);

  static {
    MONGO.start();
    System.setProperty("spring.data.mongodb.uri", MONGO.getConnectionString());
    System.setProperty("spring.data.mongodb.replica-set-name", "");
  }

  @PreDestroy
  public void shutdown() {
    MONGO.stop();
  }
}
