package com.epam.gymapp.repository;

import com.epam.gymapp.model.Trainee;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeRepository extends AbstractHibernateRepository
    implements GeneralRepository<Long, Trainee> {

  public TraineeRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public Trainee save(Trainee trainee) {
    return executeTransactionalOperation(session -> {
      session.persist(trainee);
      return trainee;
    });
  }

  @Override
  public List<Trainee> findAll() {
    return executeOperation(session -> session
        .createQuery("""
              from Trainee t
              join fetch t.user
              left join fetch t.trainers tr
              left join fetch tr.user
              left join fetch tr.specialization
              """, Trainee.class)
        .setReadOnly(true)
        .list()
    );
  }

  @Override
  public Optional<Trainee> findById(Long id) {
    return executeOperation(session -> session
        .createQuery("""
              from Trainee t
              join fetch t.user
              left join fetch t.trainers tr
              left join fetch tr.user
              left join fetch tr.specialization
              where t.id = :id
              """, Trainee.class)
        .setParameter("id", id)
        .setReadOnly(true)
        .uniqueResultOptional()
    );
  }

  public Optional<Trainee> findByUsername(String username) {
    return executeOperation(session -> session
        .createQuery("""
              from Trainee t
              join fetch t.user
              left join fetch t.trainers tr
              left join fetch tr.user
              left join fetch tr.specialization
              where t.user.username = :username
              """, Trainee.class)
        .setParameter("username", username)
        .setReadOnly(true)
        .uniqueResultOptional()
    );
  }

  @Override
  public Trainee update(Trainee trainee) {
    return executeTransactionalOperation(session -> {
      Trainee updatedTrainee = session.merge(trainee);
      return session.createQuery("""
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
    });
  }

  @Override
  public void delete(Trainee trainee) {
    executeTransactionalOperation(session -> {
      session.createMutationQuery("delete from Training t where t.trainee.id = :traineeId")
          .setParameter("traineeId", trainee.getId())
          .executeUpdate();
      session.remove(trainee);
      return null;
    });
  }
}
