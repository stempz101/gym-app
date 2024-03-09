package com.epam.gymapp.repository;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public abstract class AbstractHibernateRepository {

  private final static Logger log = LoggerFactory.getLogger(AbstractHibernateRepository.class);

  private final SessionFactory sessionFactory;

  protected <R> R executeOperation(HibernateOperation<R> hibernateOperation) {
    try (Session session = sessionFactory.openSession()) {
      return hibernateOperation.execute(session);
    }
  }

  protected <R> R executeTransactionalOperation(HibernateOperation<R> hibernateOperation) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();

      try {
        R result = hibernateOperation.execute(session);
        transaction.commit();

        return result;
      } catch (Exception ex) {
        log.error("executeTransactionalOperation: exception {}", ex.getMessage(), ex);
        transaction.rollback();

        throw new PersistenceException("Hibernate operation failed", ex);
      }
    }
  }
}
