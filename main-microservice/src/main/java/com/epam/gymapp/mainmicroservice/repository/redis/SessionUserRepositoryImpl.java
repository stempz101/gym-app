package com.epam.gymapp.mainmicroservice.repository.redis;

import com.epam.gymapp.mainmicroservice.model.SessionUser;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessionUserRepositoryImpl implements SessionUserRepository {

  private final RedisTemplate<String, SessionUser> redisTemplate;

  @Value("${application.security.token.expiration-period}")
  private int expirationPeriod;

  @Override
  public void save(String id, SessionUser sessionUser) {
    redisTemplate.opsForValue().set(id, sessionUser, expirationPeriod, TimeUnit.MILLISECONDS);
  }

  @Override
  public Optional<SessionUser> findById(String id) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(id));
  }

  @Override
  public void deleteByUsername(String username) {
    Set<String> ids = redisTemplate.keys(username + ":*");
    if (ids != null && !ids.isEmpty()) {
      redisTemplate.delete(ids);
    }
  }
}
