package com.epam.gymapp.mainmicroservice.service;

import com.epam.gymapp.mainmicroservice.model.SessionUser;
import com.epam.gymapp.mainmicroservice.model.User;
import com.epam.gymapp.mainmicroservice.repository.SessionUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtService {

  private final SessionUserRepository sessionUserRepository;

  @Value("${application.security.token.expiration-period}")
  protected int expirationPeriod;

  @Value("${application.security.token.secret-key}")
  private String secretKey;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractHash(String token) {
    return extractClaim(token, claims -> claims.get("hash")).toString();
  }

  public String generateAndSaveToken(User user) {
    String token = generateToken(user);
    deleteSessionUsers(user.getUsername());
    saveSessionUser(user);
    return token;
  }

  public String generateToken(User user) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("hash", user.getHash());
    return generateToken(extraClaims, user);
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

  public boolean isTokenValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(token));
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  protected SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private void deleteSessionUsers(String username) {
    sessionUserRepository.deleteByUsername(username);
  }

  private void saveSessionUser(User user) {
    String id = buildSessionUserId(user.getUsername(), user.getHash());
    SessionUser sessionUser = new SessionUser(user.getUsername(), user.getCreatedAt());
    sessionUserRepository.save(id, sessionUser);
  }

  public String buildSessionUserId(String username, String hash) {
    return String.format("%s:%s", username, hash);
  }

  public Optional<String> extractBearerToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
        .filter(authHeader -> authHeader.startsWith("Bearer "))
        .map(authHeader -> authHeader.substring(7));
  }
}
