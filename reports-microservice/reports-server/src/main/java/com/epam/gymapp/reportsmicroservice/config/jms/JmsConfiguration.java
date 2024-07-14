package com.epam.gymapp.reportsmicroservice.config.jms;

import com.epam.gymapp.reportsmicroservice.context.TransactionContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ErrorHandler;

@Configuration
@Profile({"default", "dev"})
public class JmsConfiguration {

  private static final Logger log = LoggerFactory.getLogger(JmsConfiguration.class);

  @Value("${application.logging.transaction-id.key}")
  private String transactionIdKey;

  @Value("${application.logging.transaction-id.header}")
  private String transactionIdHeader;

  @Bean
  @Profile({"default", "dev"})
  public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    converter.setObjectMapper(objectMapper);
    return converter;
  }

  @Bean
  @Profile({"default", "dev"})
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
      ConnectionFactory connectionFactory,
      MessageConverter jacksonJmsMessageConverter,
      PlatformTransactionManager jmsTransactionManager,
      ErrorHandler jmsErrorHandler
  ) {
    CustomJmsListenerContainerFactory factory = new CustomJmsListenerContainerFactory();

    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jacksonJmsMessageConverter);
    factory.setTransactionManager(jmsTransactionManager);
    factory.setErrorHandler(jmsErrorHandler);
    factory.setTransactionIdHeader(transactionIdHeader);
    factory.setTransactionIdKey(transactionIdKey);
    return factory;
  }

  @Bean
  @Profile({"default", "dev"})
  public ErrorHandler jmsErrorHandler() {
    return t -> {
      log.error("Handling error in listening for messages, error: " + t.getMessage(), t);
      MDC.remove(transactionIdKey);
      TransactionContextHolder.clearTransactionIdFromContext();
    };
  }

  @Bean
  @Profile({"default", "dev"})
  public PlatformTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
    return new JmsTransactionManager(connectionFactory);
  }
}
