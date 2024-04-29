package com.epam.gymapp.mainmicroservice.test.utils;

import com.epam.gymapp.mainmicroservice.model.User;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import com.epam.gymapp.mainmicroservice.service.JwtService;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;

public class JwtTokenTestUtil extends JwtService {

  public JwtTokenTestUtil(SessionUserRepository sessionUserRepository) {
    super(sessionUserRepository);
  }

  public static final String TEST_JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJoYXNoIjoiZmI2YWJlZGRiMjcwNzJiMGFhZTYwNWNiMDFhZmRiMTkiLCJzdWIiOiJLZWsuS2VrMiIsImlhdCI6MTcxMzE5MjY5MSwiZXhwIjoxNzEzMjM1ODkxfQ.9p-bwo7TyiGnVJAp-zluLfsXR_4OxvQJHWshIHaNgHM";

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
