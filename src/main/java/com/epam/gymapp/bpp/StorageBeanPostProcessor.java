package com.epam.gymapp.bpp;

import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class StorageBeanPostProcessor implements BeanPostProcessor {

  private static final Logger log = LoggerFactory.getLogger(StorageBeanPostProcessor.class);

  @Value("${data.trainee.path}")
  private String traineeDataPath;

  @Value("${data.trainer.path}")
  private String trainerDataPath;

  @Value("${data.training.path}")
  private String trainingDataPath;

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof Map) {
      switch (beanName) {
        case "traineeStorage" -> initTraineeData((Map<Long, Trainee>) bean);
        case "trainerStorage" -> initTrainerData((Map<Long, Trainer>) bean);
        case "trainingStorage" -> initTrainingData((Map<Long, Training>) bean);
      }
    }
    return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
  }

  private void initTraineeData(Map<Long, Trainee> traineeStorage) {
    log.debug("Starting to fetch trainees from {}", Paths.get(traineeDataPath).toAbsolutePath());
    List<Trainee> trainees = fetchObjects(traineeDataPath, Trainee.class);

    log.debug("Inserting fetched trainees to Trainee Storage: {}", trainees);
    trainees.forEach(trainee -> traineeStorage.put(trainee.getUserId(), trainee));
  }

  private void initTrainerData(Map<Long, Trainer> trainerStorage) {
    log.debug("Starting to fetch trainers from {}", Paths.get(trainerDataPath).toAbsolutePath());
    List<Trainer> trainers = fetchObjects(trainerDataPath, Trainer.class);

    log.debug("Inserting fetched trainers to Trainer Storage: {}", trainers);
    trainers.forEach(trainer -> trainerStorage.put(trainer.getUserId(), trainer));
  }

  private void initTrainingData(Map<Long, Training> trainingStorage) {
    log.debug("Starting to fetch trainings from {}", Paths.get(trainingDataPath).toAbsolutePath());
    List<Training> trainings = fetchObjects(trainingDataPath, Training.class);

    log.debug("Inserting fetched trainings to Training Storage: {}", trainings);
    trainings.forEach(training -> trainingStorage.put(training.getId(), training));
  }

  private <T> List<T> fetchObjects(String filePath, Class<T> objectClass) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.findAndRegisterModules();

      File jsonFile = new File(filePath);

      return objectMapper.readValue(jsonFile,
            objectMapper.getTypeFactory().constructCollectionType(List.class, objectClass));
    } catch (IOException e) {
      log.error("fetchObjects: exception {}", e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  public void setTraineeDataPath(String traineeDataPath) {
    this.traineeDataPath = traineeDataPath;
  }

  public void setTrainerDataPath(String trainerDataPath) {
    this.trainerDataPath = trainerDataPath;
  }

  public void setTrainingDataPath(String trainingDataPath) {
    this.trainingDataPath = trainingDataPath;
  }
}
