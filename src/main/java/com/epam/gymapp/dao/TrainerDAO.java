package com.epam.gymapp.dao;

import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.utils.UserUtils;
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
public class TrainerDAO implements GeneralDAO<Trainer>, InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(TrainerDAO.class);

  private final Map<Long, Trainer> trainerStorage;
  private long counter;

  @Override
  public void afterPropertiesSet() {
    log.debug("Setting up a counter to indicate an ID for a new Trainer");

    if (!trainerStorage.isEmpty()) {
      counter = trainerStorage.keySet().stream().reduce(0L, Long::max);
    }

    log.debug("The new Trainer ID will be: {}", counter);
  }

  @Override
  public Trainer save(Trainer trainer) {
    log.debug("Saving a new Trainer: {}", trainer);

    return trainer == null ? null :
        trainer.getUserId() == null ? insert(trainer) : update(trainer);
  }

  @Override
  public List<Trainer> findAll() {
    log.debug("Getting all Trainers");

    return trainerStorage.values().stream().toList();
  }

  @Override
  public Optional<Trainer> findById(Long id) {
    log.debug("Getting a Trainer by ID: {}", id);

    return Optional.ofNullable(trainerStorage.get(id));
  }

  private Trainer insert(Trainer trainer) {
    log.debug("Inserting a new Trainer to the storage: {}", trainer);

    trainer.setUserId(++counter);
    trainerStorage.put(counter, trainer);

    log.debug("Inserting a new Trainer to the storage was successful: {}", trainer);
    return trainer;
  }

  private Trainer update(Trainer trainer) {
    log.debug("Getting a persistent Trainer for updating by ID: {}", trainer.getUserId());
    Trainer persistentTrainer = trainerStorage.get(trainer.getUserId());

    log.debug("Starting an update of Trainer: {}", persistentTrainer);
    if (trainer.getFirstName() != null && !trainer.getFirstName().isBlank()) {
      persistentTrainer.setFirstName(trainer.getFirstName());
    }
    if (trainer.getLastName() != null && !trainer.getLastName().isBlank()) {
      persistentTrainer.setLastName(trainer.getLastName());
    }
    if (trainer.getUsername() != null && !trainer.getUsername().isBlank()) {
      persistentTrainer.setUsername(trainer.getUsername());
    }
    if (trainer.getPassword() != null && trainer.getPassword().length > 0) {
      persistentTrainer.setPassword(trainer.getPassword());
    }
    if (trainer.getSpecialization() != null) {
      persistentTrainer.setSpecialization(trainer.getSpecialization());
    }
    persistentTrainer.setActive(trainer.isActive());
    log.debug("Trainer with ID={} has been updated successfully: {}",
        persistentTrainer.getUserId(), persistentTrainer);

    return persistentTrainer;
  }

  public int getLastCountedAppearances(String firstName, String lastName) {
    String searchedUsername = String.format("%s.%s", firstName, lastName);

    log.debug("Getting last counted appearances of a Trainer's username: {}", searchedUsername);
    return trainerStorage.values().stream()
        .filter(trainer -> trainer.getUsername()
            .startsWith(String.format("%s.%s", firstName, lastName)))
        .mapToInt(trainer -> UserUtils.getAppearanceFromFoundUsername(trainer.getUsername()))
        .max()
        .orElse(0);
  }

  public boolean existsById(Long id) {
    log.debug("Checking existence of the Trainer by ID: {}", id);

    return trainerStorage.get(id) != null;
  }

  public boolean existsByUsername(String username) {
    log.debug("Checking existence of the Trainer by username: {}", username);

    return trainerStorage.values().stream()
        .anyMatch(trainer -> trainer.getUsername().equalsIgnoreCase(username));
  }
}
