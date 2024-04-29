package com.epam.gymapp.reportsmicroservice.config;

import jakarta.jms.ConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.transaction.PlatformTransactionManager;

@TestConfiguration
public class TestJmsConfiguration {

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
