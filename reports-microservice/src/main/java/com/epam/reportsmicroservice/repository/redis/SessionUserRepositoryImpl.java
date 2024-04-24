package com.epam.reportsmicroservice.repository.redis;

import com.epam.reportsmicroservice.model.SessionUser;
import com.epam.reportsmicroservice.repository.SessionUserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionUserRepositoryImpl implements SessionUserRepository {

  private final RedisTemplate<String, SessionUser> redisTemplate;

  @Override
  public Optional<SessionUser> findById(String id) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(id));
  }
}
