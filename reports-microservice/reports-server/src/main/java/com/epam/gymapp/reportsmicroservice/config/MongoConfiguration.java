package com.epam.gymapp.reportsmicroservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MongoConfiguration {


  @Bean
  public PlatformTransactionManager mongoTransactionManager(MongoDatabaseFactory factory) {
    return new MongoTransactionManager(factory);
  }
}
