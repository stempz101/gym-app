package com.epam.gymapp.reportsmicroservice.bdd.integration;

import com.epam.gymapp.reportsmicroservice.ReportsMicroserviceApplication;
import com.epam.gymapp.reportsmicroservice.bdd.config.CucumberSpringConfiguration;
import com.epam.gymapp.reportsmicroservice.repository.dev.TrainerWorkloadMongoRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.jms.ConnectionFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ReportsMicroserviceApplication.class)
public class CucumberIntegrationTestConfiguration extends CucumberSpringConfiguration {

  @SpyBean
  private TrainerWorkloadMongoRepository trainerWorkloadRepository;

  @Bean
  public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,
      MessageConverter jacksonJmsMessageConverter){
    JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
    jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
    jmsTemplate.setDeliveryPersistent(true);
    jmsTemplate.setSessionTransacted(true);
    jmsTemplate.setReceiveTimeout(10000);
    return jmsTemplate;
  }
}
