package com.epam.gymapp.config;

import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

  @Value("${database.url}")
  private String databaseUrl;

  @Value("${database.username}")
  private String databaseUsername;

  @Value("${database.password}")
  private String databasePassword;

  @Bean
  public SessionFactory sessionFactory() {
    org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
    configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
    configuration.setProperty("hibernate.connection.url", databaseUrl);
    configuration.setProperty("hibernate.connection.username", databaseUsername);
    configuration.setProperty("hibernate.connection.password", databasePassword);
    configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    configuration.setProperty("hibernate.hbm2ddl.auto", "update");
//    configuration.setProperty("hibernate.show.sql", "true");

    configuration.addAnnotatedClass(User.class);
    configuration.addAnnotatedClass(TrainingType.class);
    configuration.addAnnotatedClass(Trainee.class);
    configuration.addAnnotatedClass(Trainer.class);
    configuration.addAnnotatedClass(Training.class);

    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties()).build();

    return configuration.buildSessionFactory(serviceRegistry);
  }
}
