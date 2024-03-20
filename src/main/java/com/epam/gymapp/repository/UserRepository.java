package com.epam.gymapp.repository;

import com.epam.gymapp.model.User;
import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends AbstractHibernateRepository
    implements GeneralRepository<Long, User> {

  public UserRepository(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  @Override
  public User save(User user) {
    return executeTransactionalOperation(session -> {
      session.persist(user);
      return user;
    });
  }

  @Override
  public List<User> findAll() {
    return executeOperation(session -> session
        .createQuery("from User", User.class)
        .setReadOnly(true)
        .list());
  }

  public List<User> findAllByFirstAndLastNames(String firstName, String lastName) {
    return executeOperation(session -> {
      String usernamePrefix = String.format("%s.%s", firstName, lastName);

      return session.createQuery("from User u where u.username like :username", User.class)
          .setParameter("username", usernamePrefix + "%")
          .setReadOnly(true)
          .list();
    });
  }

  @Override
  public Optional<User> findById(Long id) {
    return executeOperation(session -> {
      session.setDefaultReadOnly(true);
      return Optional.ofNullable(session.get(User.class, id));
    });
  }

  public Optional<User> findByUsername(String username) {
    return executeOperation(session -> session.createQuery(
            "from User u where lower(u.username)=lower(:username)", User.class)
        .setParameter("username", username)
        .setReadOnly(true)
        .uniqueResultOptional());
  }

  @Override
  public User update(User user) {
    return executeTransactionalOperation(session -> session.merge(user));
  }

  @Override
  public void delete(User user) {
    executeTransactionalOperation(session -> {
      session.remove(user);
      return null;
    });
  }
}
