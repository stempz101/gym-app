package com.epam.reportsmicroservice.test.utils;

import com.epam.reportsmicroservice.model.security.User;
import com.epam.reportsmicroservice.service.JwtService;
import io.jsonwebtoken.Jwts;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.DigestUtils;

public class JwtTokenTestUtil extends JwtService {

  private final int expirationPeriod;

  public JwtTokenTestUtil(int expirationPeriod) {
    this.expirationPeriod = expirationPeriod;
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("hash", generateHash(userDetails));
    return generateToken(extraClaims, userDetails);
  }

  private String generateHash(UserDetails userDetails) {
    return DigestUtils.md5DigestAsHex(
        String.format("%s_%s", userDetails.getUsername(),
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).getBytes()
    );
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expirationPeriod))
        .signWith(getSigningKey())
        .compact();
  }

  public String generateExpiredToken(Map<String, Object> extraClaims, User user) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis() - expirationPeriod * 2L))
        .expiration(new Date(System.currentTimeMillis() - expirationPeriod))
        .signWith(getSigningKey())
        .compact();
  }
}
