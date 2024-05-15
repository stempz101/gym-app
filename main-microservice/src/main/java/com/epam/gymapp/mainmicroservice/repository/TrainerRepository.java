package com.epam.gymapp.mainmicroservice.repository;

import com.epam.gymapp.mainmicroservice.model.Trainer;
import com.epam.gymapp.mainmicroservice.model.custom.TrainerWorkload;
import java.time.LocalDate;
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

  @Query("""
      select new com.epam.gymapp.mainmicroservice.model.custom.TrainerWorkload(
        tu.username, tu.firstName, tu.lastName, tu.isActive,
        tr.training_year, tr.training_month, tr.training_duration
      )
      from (
        select t.trainer.id trainer_id,
          extract(year from t.date) training_year,
          extract(month from t.date) training_month,
          sum(t.duration) training_duration
        from Training t
        join t.trainee trainee
        join trainee.user trainee_user
        where trainee_user.username = :traineeUsername and t.date > cast(:date as date)
        group by training_year, training_month, trainer_id
      ) tr
      join Trainer trainer on tr.trainer_id = trainer.id
      join User tu on trainer.user.id = tu.id
      """)
  List<TrainerWorkload> findTrainersWorkloadByTraineeUsernameAfterDate(String traineeUsername, LocalDate date);

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
