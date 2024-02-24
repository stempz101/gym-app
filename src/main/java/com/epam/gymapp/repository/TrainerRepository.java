package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.TrainingType;
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
public class TrainerRepository implements GeneralRepository<Long, Trainer> {

  private static final Logger log = LoggerFactory.getLogger(TrainerRepository.class);

  private final SessionFactory sessionFactory;

  @Override
  public Trainer save(Trainer trainer) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      Trainer savedTrainer = null;

      try {
        session.createQuery("from TrainingType tt where tt.name = :name", TrainingType.class)
            .setParameter("name", trainer.getSpecialization().getName())
            .uniqueResultOptional()
            .ifPresent(trainer::setSpecialization);

        session.persist(trainer);
        savedTrainer = trainer;
        transaction.commit();
      } catch (Exception ex) {
        log.error("save: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }

      return savedTrainer;
    }
  }

  @Override
  public List<Trainer> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Trainer t
              join fetch t.user
              join fetch t.specialization
              left join fetch t.trainees tr
              left join fetch tr.user
              """, Trainer.class)
          .setReadOnly(true)
          .list();
    }
  }

  public List<Trainer> findAllUnassignedByTraineeUsername(String traineeUsername) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Trainer tr1
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
              """, Trainer.class)
          .setParameter("traineeUsername", traineeUsername)
          .setReadOnly(true)
          .list();
    }
  }

  public List<Trainer> findAllByUsernameIn(List<String> usernames) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Trainer t
              join fetch t.user u
              join fetch t.specialization
              left join fetch t.trainees
              where u.username in (:usernames)
              """, Trainer.class)
          .setParameter("usernames", usernames)
          .setReadOnly(true)
          .list();
    }
  }

  @Override
  public Optional<Trainer> findById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Trainer t
              join fetch t.user
              join fetch t.specialization
              left join fetch t.trainees tr
              left join fetch tr.user
              where t.id = :id
              """, Trainer.class)
          .setParameter("id", id)
          .setReadOnly(true)
          .uniqueResultOptional();
    }
  }

  public Optional<Trainer> findByUsername(String username) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Trainer t
              join fetch t.user
              join fetch t.specialization
              left join fetch t.trainees tr
              left join fetch tr.user
              where t.user.username = :username
              """, Trainer.class)
          .setParameter("username", username)
          .setReadOnly(true)
          .uniqueResultOptional();
    }
  }

  @Override
  public Trainer update(Trainer trainer) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      Trainer updatedTrainer = null;

      try {
        updatedTrainer = session.merge(trainer);
        updatedTrainer = session.createQuery("""
                from Trainer t
                join fetch t.user
                join fetch t.specialization
                left join fetch t.trainees tr
                left join fetch tr.user
                where t.id = :id
                """, Trainer.class)
            .setParameter("id", updatedTrainer.getId())
            .setReadOnly(true)
            .uniqueResult();
        transaction.commit();
      } catch (Exception ex) {
        log.error("update: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }

      return updatedTrainer;
    }
  }

  @Override
  public void delete(Trainer trainer) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();

      try {
        session.createMutationQuery("delete from Training t where t.trainer.id = :trainerId")
            .setParameter("trainerId", trainer.getId())
            .executeUpdate();
        session.remove(trainer);
        transaction.commit();
      } catch (Exception ex) {
        log.error("delete: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }
    }
  }
}
