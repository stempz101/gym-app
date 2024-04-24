package com.epam.gymapp.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

  private static final Logger log = LoggerFactory.getLogger(LoginAttemptService.class);

  public static final int MAX_ATTEMPT = 3;

  private final HttpServletRequest httpServletRequest;

  private LoadingCache<String, Integer> attemptsCache;

  @PostConstruct
  public void setUp() {
    attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES)
        .build(new CacheLoader<>() {
          @Override
          public Integer load(String key) {
            return 0;
          }
        });
  }

  public void loginFailed(String key) {
    int attempts;
    try {
      attempts = attemptsCache.get(key);
    } catch (ExecutionException e) {
      attempts = 0;
    }
    attempts++;
    attemptsCache.put(key, attempts);
    log.info("Login failed {} time(s)", attempts);
  }

  public boolean isBlocked() {
    try {
      boolean blocked = attemptsCache.get(getClientIP()) >= MAX_ATTEMPT;
      if (blocked) {
        log.warn("Too many attempts to authenticate. Authentication is blocked!");
      }
      return blocked;
    } catch (ExecutionException e) {
      return false;
    }
  }

  private String getClientIP() {
    String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");
    if (xfHeader != null) {
      return xfHeader.split(",")[0];
    }
    return httpServletRequest.getRemoteAddr();
  }
}
