package com.epam.gymapp.dao;

import com.epam.gymapp.model.Training;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingDAO implements GeneralDAO<Training>, InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(TrainingDAO.class);

  private final Map<Long, Training> trainingStorage;
  private long counter;

  @Override
  public void afterPropertiesSet() {
    log.debug("Setting up a counter to indicate an ID for a new Training");

    if (!trainingStorage.isEmpty()) {
      counter = trainingStorage.keySet().stream().reduce(0L, Long::max);
    }

    log.debug("The new Training ID will be: {}", counter);
  }

  @Override
  public Training save(Training training) {
    log.debug("Saving a new Training: {}", training);

    return training == null ? null : insert(training);
  }

  @Override
  public List<Training> findAll() {
    log.debug("Getting all Trainings");

    return trainingStorage.values().stream().toList();
  }

  @Override
  public Optional<Training> findById(Long id) {
    log.debug("Getting a Training by ID: {}", id);

    return Optional.ofNullable(trainingStorage.get(id));
  }

  private Training insert(Training training) {
    log.debug("Inserting a new Training to the storage: {}", training);

    training.setId(++counter);
    trainingStorage.put(counter, training);

    log.debug("Inserting a new Training to the storage was successful: {}", training);
    return training;
  }
}
