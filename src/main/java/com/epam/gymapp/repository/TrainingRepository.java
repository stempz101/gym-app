package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingRepository extends AbstractHibernateRepository
    implements GeneralRepository<Long, Training> {

  public TrainingRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public Training save(Training training) {
    return executeTransactionalOperation(session -> {
      Trainee trainee = session.merge(training.getTrainee());
      Trainer trainer = session.merge(training.getTrainer());
      training.setTrainee(trainee);
      training.setTrainer(trainer);

      session.persist(training);
      return training;
    });
  }

  @Override
  public List<Training> findAll() {
    return executeOperation(session -> session
        .createQuery("""
              from Training t
              join fetch t.trainee trainee
              join fetch trainee.user
              join fetch t.trainer trainer
              join fetch trainer.user
              join fetch t.type
              """, Training.class)
        .setReadOnly(true)
        .list());
  }

  public List<Training> findAllByTraineeUsernameAndParams(String username, LocalDate fromDate,
      LocalDate toDate, String trainerName, String trainingType) {
    return executeOperation(session -> {
      session.setDefaultReadOnly(true);

      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Training> query = builder.createQuery(Training.class);
      Root<Training> root = query.from(Training.class);

      root.fetch("trainee", JoinType.INNER).fetch("user", JoinType.INNER);
      root.fetch("trainer", JoinType.INNER).fetch("user", JoinType.INNER);
      root.fetch("type", JoinType.INNER);

      Predicate predicate = builder.equal(root.get("trainee").get("user").get("username"),
          username);

      if (fromDate != null) {
        predicate = builder.and(predicate,
            builder.greaterThanOrEqualTo(root.get("date"), fromDate));
      }
      if (toDate != null) {
        predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("date"), toDate));
      }
      if (trainerName != null) {
        Expression<String> fullNameExpression = builder.concat(
            root.get("trainer").get("user").get("firstName"), " ");
        fullNameExpression = builder.concat(
            fullNameExpression, root.get("trainer").get("user").get("lastName"));
        Expression<String> fullNameLowerCase = builder.lower(fullNameExpression);
        Expression<String> trainerNameLowerCase = builder.lower(builder.literal(trainerName + "%"));
        predicate = builder.and(predicate, builder.like(fullNameLowerCase, trainerNameLowerCase));
      }
      if (trainingType != null) {
        predicate = builder.and(predicate,
            builder.equal(root.get("type").get("name"), trainingType));
      }

      query.select(root).where(predicate);

      return session.createQuery(query).getResultList();
    });
  }

  public List<Training> findAllByTrainerUsernameAndParams(String username, LocalDate fromDate,
      LocalDate toDate, String traineeName) {
    return executeOperation(session -> {
      session.setDefaultReadOnly(true);

      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Training> query = builder.createQuery(Training.class);
      Root<Training> root = query.from(Training.class);

      root.fetch("trainee", JoinType.INNER).fetch("user", JoinType.INNER);
      root.fetch("trainer", JoinType.INNER).fetch("user", JoinType.INNER);
      root.fetch("type", JoinType.INNER);

      Predicate predicate = builder.equal(root.get("trainer").get("user").get("username"),
          username);

      if (fromDate != null) {
        predicate = builder.and(predicate,
            builder.greaterThanOrEqualTo(root.get("date"), fromDate));
      }
      if (toDate != null) {
        predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("date"), toDate));
      }
      if (traineeName != null) {
        Expression<String> fullNameExpression = builder.concat(
            root.get("trainee").get("user").get("firstName"), " ");
        fullNameExpression = builder.concat(
            fullNameExpression, root.get("trainee").get("user").get("lastName"));
        Expression<String> fullNameLowerCase = builder.lower(fullNameExpression);
        Expression<String> traineeNameLowerCase = builder.lower(builder.literal(traineeName + "%"));
        predicate = builder.and(predicate, builder.like(fullNameLowerCase, traineeNameLowerCase));
      }

      query.select(root).where(predicate);

      return session.createQuery(query).getResultList();
    });
  }

  @Override
  public Optional<Training> findById(Long id) {
    return executeOperation(session -> session
        .createQuery("""
              from Training t
              join fetch t.trainee trainee
              join fetch trainee.user
              join fetch t.trainer trainer
              join fetch trainer.user
              join fetch t.type
              where t.id=:id
              """, Training.class)
        .setParameter("id", id)
        .setReadOnly(true)
        .uniqueResultOptional());
  }

  @Override
  public Training update(Training training) {
    return executeTransactionalOperation(session -> {
      Training updatedTraining = session.merge(training);
      return session.createQuery("""
                from Training t
                join fetch t.trainee trainee
                join fetch trainee.user
                join fetch t.trainer trainer
                join fetch trainer.user
                join fetch t.type
                where t.id=:id
                """, Training.class)
          .setParameter("id", updatedTraining.getId())
          .setReadOnly(true)
          .uniqueResult();
    });
  }

  @Override
  public void delete(Training training) {
    executeOperation(session -> {
      session.remove(training);
      return null;
    });
  }
}
