package com.epam.gymapp.reportsmicroservice.config;

import jakarta.annotation.PreDestroy;
import jakarta.jms.ConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.GenericContainer;

@TestConfiguration
public class TestJmsConfiguration {

  private static final GenericContainer<?> ACTIVE_MQ = new GenericContainer<>("rmohr/activemq:latest")
      .withExposedPorts(61616);

  static  {
    ACTIVE_MQ.start();
    System.setProperty("spring.activemq.broker-url", "tcp://localhost:" + ACTIVE_MQ.getMappedPort(61616));
    System.setProperty("spring.jta.enabled", "false");
  }

  @PreDestroy
  public void shutdown() {
    ACTIVE_MQ.stop();
  }

  @Bean
  public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,
      MessageConverter jacksonJmsMessageConverter){
    JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
    jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
    jmsTemplate.setDeliveryPersistent(true);
    jmsTemplate.setSessionTransacted(true);
    return jmsTemplate;
  }

  @Bean
  public PlatformTransactionManager transactionManager(ConnectionFactory connectionFactory) {
    return new JmsTransactionManager(connectionFactory);
  }
}
