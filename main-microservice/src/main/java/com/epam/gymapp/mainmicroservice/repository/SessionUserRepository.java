package com.epam.gymapp.mainmicroservice.repository;

import com.epam.gymapp.mainmicroservice.model.SessionUser;
import java.util.Optional;

public interface SessionUserRepository {

  void save(String id, SessionUser sessionUser);

  Optional<SessionUser> findById(String id);

  void deleteByUsername(String username);
}
