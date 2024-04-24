package com.epam.reportsmicroservice.repository;

import com.epam.reportsmicroservice.model.SessionUser;
import java.util.Optional;

public interface SessionUserRepository {

  Optional<SessionUser> findById(String id);
}
