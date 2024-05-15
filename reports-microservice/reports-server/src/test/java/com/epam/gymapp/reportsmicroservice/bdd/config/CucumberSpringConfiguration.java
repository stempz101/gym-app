package com.epam.gymapp.reportsmicroservice.bdd.config;

import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.test.utils.TrainerWorkloadTestUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;

public class CucumberSpringConfiguration {

  private static final Network NETWORK = Network.newNetwork();

  protected static final MongoDBContainer MONGO = new MongoDBContainer("mongo:latest")
      .withExposedPorts(27017)
      .withNetwork(NETWORK)
      .withNetworkAliases("M1")
      .withCommand("--replSet rs0 --bind_ip localhost,M1");

  protected static final GenericContainer<?> ACTIVE_MQ = new GenericContainer<>("rmohr/activemq:latest")
      .withExposedPorts(61616);

  static {
    MONGO.start();
    ACTIVE_MQ.start();
  }

  @Autowired
  private MongoTemplate mongoTemplate;

  @DynamicPropertySource
  public static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", MONGO::getConnectionString);
    registry.add("spring.data.mongodb.replica-set-name", () -> "rs0");
    registry.add("spring.activemq.broker-url",
        () -> "tcp://localhost:" + ACTIVE_MQ.getMappedPort(61616));
  }

  @PostConstruct
  public void init() {
    TrainerWorkload trainerWorkload1 = TrainerWorkloadTestUtil.getTrainerWorkload1();
    TrainerWorkload trainerWorkload2 = TrainerWorkloadTestUtil.getTrainerWorkload2();

    mongoTemplate.save(trainerWorkload1);
    mongoTemplate.save(trainerWorkload2);
  }

  @PreDestroy
  public void shutdown() {
    mongoTemplate.dropCollection(TrainerWorkload.class);

    MONGO.stop();
    ACTIVE_MQ.stop();
  }
}
