package com.epam.gymapp.repository;

import com.epam.gymapp.model.TrainingType;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeRepository extends AbstractHibernateRepository
    implements GeneralRepository<Long, TrainingType> {

  public TrainingTypeRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public TrainingType save(TrainingType trainingType) {
    return executeTransactionalOperation(session -> {
      session.persist(trainingType);
      return trainingType;
    });
  }

  @Override
  public List<TrainingType> findAll() {
    return executeOperation(session -> session
        .createQuery("from TrainingType", TrainingType.class)
        .setReadOnly(true)
        .list());
  }

  @Override
  public Optional<TrainingType> findById(Long id) {
    return executeOperation(session -> {
      session.setDefaultReadOnly(true);
      return Optional.ofNullable(session.get(TrainingType.class, id));
    });
  }

  @Override
  public TrainingType update(TrainingType trainingType) {
    return executeTransactionalOperation(session -> session.merge(trainingType));
  }

  @Override
  public void delete(TrainingType trainingType) {
    executeTransactionalOperation(session -> {
      session.remove(trainingType);
      return null;
    });
  }
}
