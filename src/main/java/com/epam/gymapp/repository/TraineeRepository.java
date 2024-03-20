package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends CrudRepository<Trainee, Long> {

  @Override
  @NonNull
  @Query("""
      select t from Trainee t
      join fetch t.user
      left join fetch t.trainers tr
      left join fetch tr.user
      left join fetch tr.specialization
      """)
  List<Trainee> findAll();

  @Override
  @NonNull
  @Query("""
      select t from Trainee t
      join fetch t.user
      left join fetch t.trainers tr
      left join fetch tr.user
      left join fetch tr.specialization
      where t.id = :id
      """)
  Optional<Trainee> findById(@NonNull Long id);

  @Query("""
      select t from Trainee t
      join fetch t.user
      left join fetch t.trainers tr
      left join fetch tr.user
      left join fetch tr.specialization
      where t.user.username = :username
      """)
  Optional<Trainee> findByUsername(String username);
}
