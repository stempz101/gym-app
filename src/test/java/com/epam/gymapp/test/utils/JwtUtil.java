package com.epam.gymapp.test.utils;

import com.epam.gymapp.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;

public class JwtUtil {

  public static final int EXPIRATION_PERIOD_OF_TOKENS = 1000 * 60 * 60 * 12;
  private static final String SECRET_KEY = "6u21QAFzzxbE4v9y8cVyE73vTnHgdb0jn3APHdwJESJaY8nQxNYFaSejvXiU";

  public static String generateToken(Map<String, Object> extraClaims, User user) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_PERIOD_OF_TOKENS))
        .signWith(getSigningKey())
        .compact();
  }

  public static String generateExpiredToken(Map<String, Object> extraClaims, User user) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis() - EXPIRATION_PERIOD_OF_TOKENS * 2))
        .expiration(new Date(System.currentTimeMillis() - EXPIRATION_PERIOD_OF_TOKENS))
        .signWith(getSigningKey())
        .compact();
  }

  private static SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
