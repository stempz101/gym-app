package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainee;
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
public class TraineeRepository implements GeneralRepository<Long, Trainee> {

  private static final Logger log = LoggerFactory.getLogger(TraineeRepository.class);

  private final SessionFactory sessionFactory;

  @Override
  public Trainee save(Trainee trainee) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      Trainee savedTrainee = null;

      try {
        session.persist(trainee);
        savedTrainee = trainee;
        transaction.commit();
      } catch (Exception ex) {
        log.error("save: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }

      return savedTrainee;
    }
  }

  @Override
  public List<Trainee> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Trainee t
              join fetch t.user
              left join fetch t.trainers tr
              left join fetch tr.user
              left join fetch tr.specialization
              """, Trainee.class)
          .setReadOnly(true)
          .list();
    }
  }

  @Override
  public Optional<Trainee> findById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Trainee t
              join fetch t.user
              left join fetch t.trainers tr
              left join fetch tr.user
              left join fetch tr.specialization
              where t.id = :id
              """, Trainee.class)
          .setParameter("id", id)
          .setReadOnly(true)
          .uniqueResultOptional();
    }
  }

  public Optional<Trainee> findByUsername(String username) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("""
              from Trainee t
              join fetch t.user
              left join fetch t.trainers tr
              left join fetch tr.user
              left join fetch tr.specialization
              where t.user.username = :username
              """, Trainee.class)
          .setParameter("username", username)
          .setReadOnly(true)
          .uniqueResultOptional();
    }
  }

  @Override
  public Trainee update(Trainee trainee) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      Trainee updatedTrainee = null;

      try {
        updatedTrainee = session.merge(trainee);
        updatedTrainee = session.createQuery("""
                from Trainee t
                join fetch t.user
                left join fetch t.trainers tr
                left join fetch tr.user
                left join fetch tr.specialization
                where t.id = :id
                """, Trainee.class)
            .setParameter("id", updatedTrainee.getId())
            .setReadOnly(true)
            .uniqueResult();
        transaction.commit();
      } catch (Exception ex) {
        log.error("update: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }

      return updatedTrainee;
    }
  }

  @Override
  public void delete(Trainee trainee) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();

      try {
        session.createMutationQuery("delete from Training t where t.trainee.id = :traineeId")
            .setParameter("traineeId", trainee.getId())
            .executeUpdate();
        session.remove(trainee);
        transaction.commit();
      } catch (Exception ex) {
        log.error("delete: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }
    }
  }
}
