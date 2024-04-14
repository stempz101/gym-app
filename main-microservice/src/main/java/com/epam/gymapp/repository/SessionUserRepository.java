package com.epam.gymapp.repository;

import com.epam.gymapp.model.SessionUser;
import java.util.Optional;

public interface SessionUserRepository {

  void save(String id, SessionUser sessionUser);

  Optional<SessionUser> findById(String id);

  void deleteByUsername(String username);
}
