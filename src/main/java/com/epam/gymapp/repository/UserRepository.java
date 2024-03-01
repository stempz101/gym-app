package com.epam.gymapp.repository;

import com.epam.gymapp.model.User;
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
public class UserRepository implements GeneralRepository<Long, User> {

  private static final Logger log = LoggerFactory.getLogger(UserRepository.class);

  private final SessionFactory sessionFactory;

  @Override
  public User save(User user) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      User savedUser = null;

      try {
        session.persist(user);
        savedUser = user;
        transaction.commit();
      } catch (Exception ex) {
        log.error("save: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }

      return savedUser;
    }
  }

  @Override
  public List<User> findAll() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("from User", User.class).setReadOnly(true).list();
    }
  }

  public List<User> findAllByFirstAndLastNames(String firstName, String lastName) {
    try (Session session = sessionFactory.openSession()) {
      String usernamePrefix = String.format("%s.%s", firstName, lastName);

      return session.createQuery("from User u where u.username like :username", User.class)
          .setParameter("username", usernamePrefix + "%")
          .setReadOnly(true)
          .list();
    }
  }

  @Override
  public Optional<User> findById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      session.setDefaultReadOnly(true);
      return Optional.ofNullable(session.get(User.class, id));
    }
  }

  public Optional<User> findByUsername(String username) {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery(
              "from User u where lower(u.username)=lower(:username)", User.class)
          .setParameter("username", username)
          .setReadOnly(true)
          .uniqueResultOptional();
    }
  }

  @Override
  public User update(User user) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();
      User updatedUser = null;

      try {
        updatedUser = session.merge(user);
        transaction.commit();
      } catch (Exception ex) {
        log.error("update: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }

      return updatedUser;
    }
  }

  @Override
  public void delete(User user) {
    try (Session session = sessionFactory.openSession()) {
      Transaction transaction = session.beginTransaction();

      try {
        session.remove(user);
        transaction.commit();
      } catch (Exception ex) {
        log.error("delete: exception {}", ex.getMessage(), ex);
        transaction.rollback();
      }
    }
  }
}
