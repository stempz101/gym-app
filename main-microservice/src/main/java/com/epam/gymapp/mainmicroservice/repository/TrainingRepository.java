package com.epam.gymapp.mainmicroservice.repository;

import com.epam.gymapp.mainmicroservice.model.Training;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends CrudRepository<Training, Long> {

  @Override
  @NonNull
  @Query("""
      select t from Training t
      join fetch t.trainee trainee
      join fetch trainee.user
      join fetch t.trainer trainer
      join fetch trainer.user
      join fetch t.type
      """)
  List<Training> findAll();

  @Query("""
      select t from Training t
      join fetch t.trainee trainee
      join fetch trainee.user trainee_user
      join fetch t.trainer trainer
      join fetch trainer.user trainer_user
      join fetch t.type type
      where trainee_user.username = :username
      and (t.date >= cast(:fromDate as date) or cast(:fromDate as date) is null)
      and (t.date <= cast(:toDate as date) or cast(:toDate as date) is null)
      and (lower(concat(trainer_user.firstName, ' ', trainer_user.lastName)) like lower(concat(:trainerName, '%')) or :trainerName is null)
      and (type.name = :trainingType or :trainingType is null)
      """)
  List<Training> findAllByTraineeUsernameAndParams(String username, LocalDate fromDate,
      LocalDate toDate, String trainerName, String trainingType);

  @Query("""
      select t from Training t
      join fetch t.trainee trainee
      join fetch trainee.user trainee_user
      join fetch t.trainer trainer
      join fetch trainer.user trainer_user
      join fetch t.type type
      where trainer_user.username = :username
      and (t.date >= cast(:fromDate as date) or cast(:fromDate as date) is null)
      and (t.date <= cast(:toDate as date) or cast(:toDate as date) is null)
      and (lower(concat(trainee_user.firstName, ' ', trainee_user.lastName)) like lower(concat(:traineeName, '%')) or :traineeName is null)
      """)
  List<Training> findAllByTrainerUsernameAndParams(String username, LocalDate fromDate,
      LocalDate toDate, String traineeName);

  @Query("select count(t) from Training t where t.date >= :currentDate")
  long countOfUpcomingTrainings(LocalDate currentDate);

  void deleteByTraineeId(Long traineeId);
}
