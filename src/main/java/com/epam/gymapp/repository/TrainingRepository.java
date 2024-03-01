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
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrainingRepository implements GeneralRepository<Long, Training> {

  private static final Logger log = LoggerFactory.getLogger(TrainingRepository.class);

  private final SessionFactory sessionFactory;

  @Override
  public Training save(Training training) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      Training savedTraining = null;

      try {
        Trainee trainee = session.merge(training.getTrainee());
        Trainer trainer = session.merge(training.getTrainer());
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        session.persist(training);
        savedTraining = training;
        transaction.commit();
      } catch (Exception ex) {
        log.error("save: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }

      return savedTraining;
    }
  }

  @Override
  public List<Training> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Training t
              join fetch t.trainee trainee
              join fetch trainee.user
              join fetch t.trainer trainer
              join fetch trainer.user
              join fetch t.type
              """, Training.class)
          .setReadOnly(true)
          .list();
    }
  }

  public List<Training> findAllByTraineeUsernameAndParams(String username, LocalDate fromDate,
      LocalDate toDate, String trainerName, String trainingType) {
    try (Session session = sessionFactory.openSession()) {
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
    }
  }

  public List<Training> findAllByTrainerUsernameAndParams(String username, LocalDate fromDate,
      LocalDate toDate, String traineeName) {
    try (Session session = sessionFactory.openSession()) {
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
    }
  }

  @Override
  public Optional<Training> findById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
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
          .uniqueResultOptional();
    }
  }

  @Override
  public Training update(Training training) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      Training updatedTraining = null;

      try {
        updatedTraining = session.merge(training);
        updatedTraining = session.createQuery("""
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
        transaction.commit();
      } catch (Exception ex) {
        log.error("update: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }

      return updatedTraining;
    }
  }

  @Override
  public void delete(Training training) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();

      try {
        session.remove(training);
        transaction.commit();
      } catch (Exception ex) {
        log.error("delete: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }
    }
  }
}
