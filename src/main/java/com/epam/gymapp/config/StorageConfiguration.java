package com.epam.gymapp.config;

import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

  @Bean
  public Map<Long, Trainee> traineeStorage() {
    return new HashMap<>();
  }

  @Bean
  public Map<Long, Trainer> trainerStorage() {
    return new HashMap<>();
  }

  @Bean
  public Map<Long, Training> trainingStorage() {
    return new HashMap<>();
  }
}
