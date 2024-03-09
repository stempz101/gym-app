package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.TrainingType;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerRepository extends AbstractHibernateRepository
    implements GeneralRepository<Long, Trainer> {

  public TrainerRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public Trainer save(Trainer trainer) {
    return executeTransactionalOperation(session -> {
      session.createQuery("from TrainingType tt where tt.name = :name", TrainingType.class)
          .setParameter("name", trainer.getSpecialization().getName())
          .uniqueResultOptional()
          .ifPresent(trainer::setSpecialization);

      session.persist(trainer);
      return trainer;
    });
  }

  @Override
  public List<Trainer> findAll() {
    return executeOperation(session -> session
        .createQuery("""
              from Trainer t
              join fetch t.user
              join fetch t.specialization
              left join fetch t.trainees tr
              left join fetch tr.user
              """, Trainer.class)
        .setReadOnly(true)
        .list());
  }

  public List<Trainer> findAllUnassignedByTraineeUsername(String traineeUsername) {
    return executeOperation(session -> session
        .createQuery("""
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
        .list());
  }

  public List<Trainer> findAllByUsernameIn(List<String> usernames) {
    return executeOperation(session -> session
        .createQuery("""
              from Trainer t
              join fetch t.user u
              join fetch t.specialization
              left join fetch t.trainees
              where u.username in (:usernames)
              """, Trainer.class)
        .setParameter("usernames", usernames)
        .setReadOnly(true)
        .list());
  }

  @Override
  public Optional<Trainer> findById(Long id) {
    return executeOperation(session -> session
        .createQuery("""
              from Trainer t
              join fetch t.user
              join fetch t.specialization
              left join fetch t.trainees tr
              left join fetch tr.user
              where t.id = :id
              """, Trainer.class)
        .setParameter("id", id)
        .setReadOnly(true)
        .uniqueResultOptional());
  }

  public Optional<Trainer> findByUsername(String username) {
    return executeOperation(session -> session
        .createQuery("""
              from Trainer t
              join fetch t.user
              join fetch t.specialization
              left join fetch t.trainees tr
              left join fetch tr.user
              where t.user.username = :username
              """, Trainer.class)
        .setParameter("username", username)
        .setReadOnly(true)
        .uniqueResultOptional());
  }

  @Override
  public Trainer update(Trainer trainer) {
    return executeTransactionalOperation(session -> {
      Trainer updatedTrainer = session.merge(trainer);
      return session.createQuery("""
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
    });
  }

  @Override
  public void delete(Trainer trainer) {
    executeTransactionalOperation(session -> {
      session.createMutationQuery("delete from Training t where t.trainer.id = :trainerId")
          .setParameter("trainerId", trainer.getId())
          .executeUpdate();
      session.remove(trainer);
      return null;
    });
  }
}
