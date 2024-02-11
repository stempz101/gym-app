package com.epam.gymapp.dao;

import com.epam.gymapp.model.Trainee;
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
public class TraineeDAO implements GeneralDAO<Trainee>, InitializingBean {

  private final static Logger log = LoggerFactory.getLogger(TraineeDAO.class);

  private final Map<Long, Trainee> traineeStorage;
  private long counter = 0L;

  @Override
  public void afterPropertiesSet() {
    log.debug("Setting up a counter to indicate an ID for a new Trainee");

    if (!traineeStorage.isEmpty()) {
      counter = traineeStorage.keySet().stream().reduce(0L, Long::max);
    }

    log.debug("The new Trainee ID will be: {}", counter);
  }

  @Override
  public Trainee save(Trainee trainee) {
    log.debug("Saving a new Trainee: {}", trainee);

    return trainee == null ? null :
        trainee.getUserId() == null ? insert(trainee) : update(trainee);
  }

  @Override
  public List<Trainee> findAll() {
    log.debug("Getting all Trainees");

    return traineeStorage.values().stream().toList();
  }

  @Override
  public Optional<Trainee> findById(Long id) {
    log.debug("Getting a Trainee by ID: {}", id);

    return Optional.ofNullable(traineeStorage.get(id));
  }

  private Trainee insert(Trainee trainee) {
    log.debug("Inserting a new Trainee to the storage: {}", trainee);

    trainee.setUserId(++counter);
    traineeStorage.put(counter, trainee);

    log.debug("Inserting a new Trainee to the storage was successful: {}", trainee);
    return trainee;
  }

  private Trainee update(Trainee trainee) {
    log.debug("Getting a persistent Trainee for updating by ID: {}", trainee.getUserId());
    Trainee persistentTrainee = traineeStorage.get(trainee.getUserId());

    log.debug("Starting an update of Trainee: {}", persistentTrainee);
    if (trainee.getFirstName() != null && !trainee.getFirstName().isBlank()) {
      persistentTrainee.setFirstName(trainee.getFirstName());
    }
    if (trainee.getLastName() != null && !trainee.getLastName().isBlank()) {
      persistentTrainee.setLastName(trainee.getLastName());
    }
    if (trainee.getUsername() != null && !trainee.getUsername().isBlank()) {
      persistentTrainee.setUsername(trainee.getUsername());
    }
    if (trainee.getPassword() != null && trainee.getPassword().length > 0) {
      persistentTrainee.setPassword(trainee.getPassword());
    }
    if (trainee.getDateOfBirth() != null) {
      persistentTrainee.setDateOfBirth(trainee.getDateOfBirth());
    }
    if (trainee.getAddress() != null && !trainee.getAddress().isBlank()) {
      persistentTrainee.setAddress(trainee.getAddress());
    }
    persistentTrainee.setActive(trainee.isActive());
    log.debug("Trainee with ID={} has been updated successfully: {}",
        persistentTrainee.getUserId(), persistentTrainee);

    return persistentTrainee;
  }

  public void deleteById(Long id) {
    log.debug("Deleting Trainee by ID: {}", id);

    traineeStorage.remove(id);

    log.debug("Trainee with ID={} has been deleted successfully", id);
  }

  public int getLastCountedAppearances(String firstName, String lastName) {
    String searchedUsername = String.format("%s.%s", firstName, lastName);

    log.debug("Getting last counted appearances of a Trainee's username: {}", searchedUsername);
    return traineeStorage.values().stream()
        .filter(trainee -> trainee.getUsername().startsWith(searchedUsername))
        .mapToInt(trainee -> UserUtils.getAppearanceFromFoundUsername(trainee.getUsername()))
        .max()
        .orElse(0);
  }

  public boolean existsById(Long id) {
    log.debug("Checking existence of the Trainee by ID: {}", id);

    return traineeStorage.get(id) != null;
  }

  public boolean existsByUsername(String username) {
    log.debug("Checking existence of the Trainee by username: {}", username);

    return traineeStorage.values().stream()
        .anyMatch(trainee -> trainee.getUsername().equalsIgnoreCase(username));
  }
}
