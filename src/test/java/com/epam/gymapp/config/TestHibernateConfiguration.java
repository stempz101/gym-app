package com.epam.gymapp.config;

import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.epam.gymapp.repository")
public class TestHibernateConfiguration {

  @Bean
  public SessionFactory sessionFactory() {
    org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
    configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
    configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:gym_db");
    configuration.setProperty("hibernate.connection.username", "sa");
    configuration.setProperty("hibernate.connection.password", "");
    configuration.setProperty("hibernate.connection.pool_size", "1");
    configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    configuration.setProperty("hibernate.hbm2ddl.auto", "create");
    configuration.setProperty("hibernate.show.sql", "true");

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
