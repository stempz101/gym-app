package com.epam.gymapp.repository;

import com.epam.gymapp.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Query("""
      select u from User u
      where u.username like concat(:firstName, '.', :lastName, '%')
      """)
  List<User> findAllByFirstAndLastNames(String firstName, String lastName);

  Optional<User> findByUsernameIgnoreCase(String username);
}
