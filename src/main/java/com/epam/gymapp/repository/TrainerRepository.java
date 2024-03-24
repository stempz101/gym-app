package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends CrudRepository<Trainer, Long> {

  @Override
  @NonNull
  @Query("""
      select t from Trainer t
      join fetch t.user
      join fetch t.specialization
      left join fetch t.trainees tr
      left join fetch tr.user
      """)
  List<Trainer> findAll();

  @Query("""
      select tr1 from Trainer tr1
      join fetch tr1.user
      join fetch tr1.specialization
      left join fetch tr1.trainees
      where tr1.id not in (
        select tr2.id
        from Trainer tr2
        join tr2.trainees te
        join te.user ute
        where ute.username = :traineeUsername
      )
      """)
  List<Trainer> findAllUnassignedByTraineeUsername(String traineeUsername);

  @Query("""
      select t from Trainer t
      join fetch t.user u
      join fetch t.specialization
      left join fetch t.trainees
      where u.username in (:usernames)
      """)
  List<Trainer> findAllByUsernameIn(List<String> usernames);

  @Override
  @NonNull
  @Query("""
      select t from Trainer t
      join fetch t.user
      join fetch t.specialization
      left join fetch t.trainees tr
      left join fetch tr.user
      where t.id = :id
      """)
  Optional<Trainer> findById(@NonNull Long id);

  @Query("""
      select t from Trainer t
      join fetch t.user
      join fetch t.specialization
      left join fetch t.trainees tr
      left join fetch tr.user
      where t.user.username = :username
      """)
  Optional<Trainer> findByUsername(String username);
}
