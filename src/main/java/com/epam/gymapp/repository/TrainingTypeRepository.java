package com.epam.gymapp.repository;

import com.epam.gymapp.model.TrainingType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends CrudRepository<TrainingType, Long> {

  @Override
  @NonNull
  List<TrainingType> findAll();

  Optional<TrainingType> findByName(String name);
}
