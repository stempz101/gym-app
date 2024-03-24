package com.epam.gymapp.test.utils;

import com.epam.gymapp.model.JwtToken;
import com.epam.gymapp.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;

public class JwtTokenTestUtil {

  public static final int EXPIRATION_PERIOD_OF_TOKENS = 1000 * 60 * 60 * 12;
  private static final String SECRET_KEY = "mpCRcDkweGrFp98LY4iEDuPYArdA64TJbxeVKwaUH4mu4KpTXkm4HhHLZb9h";

  public static final int TEST_JWT_ID_1_USER_1 = 1;
  public static final String TEST_JWT_TOKEN_1_USER_1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNaWNoYWVsLlBhdGVsIiwiaWF0IjoxNzExMTM5Mzg1LCJleHAiOjE3MTExODI1ODV9.ca6b0zn92mHL7OYuvXxbg-8I6Tr0fIiQ7860pXPXRkU";
  public static final boolean TEST_JWT_REVOKED_1_USER_1 = true;

  public static final int TEST_JWT_ID_2_USER_1 = 2;
  public static final String TEST_JWT_TOKEN_2_USER_1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNaWNoYWVsLlBhdGVsIiwiaWF0IjoxNzExMTM5MzgyLCJleHAiOjE3MTExODI1ODV9.wRGyQJcx_vJxuypQvpK_rzPFg-lVBfYs7puJwfp3668";

  public static final int TEST_JWT_ID_3_USER_1 = 3;
  public static final String TEST_JWT_TOKEN_3_USER_1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNaWNoYWVsLlBhdGVsIiwiaWF0IjoxNzExMTM5MzgxLCJleHAiOjE3MTExODI1ODV9.TgJW3s_yc6QPjpAjzME_1fgoMEmoObjd_KgcY5iov9s";

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

  public static JwtToken getNewJwtTokenOfTrainee1() {
    User traineeUser = UserTestUtil.getTraineeUser1();
    return JwtToken.builder()
        .token(generateToken(new HashMap<>(), traineeUser))
        .user(traineeUser)
        .build();
  }

  public static JwtToken getJwtToken1OfTrainee1() {
    return JwtToken.builder()
        .id(TEST_JWT_ID_1_USER_1)
        .token(TEST_JWT_TOKEN_1_USER_1)
        .isRevoked(TEST_JWT_REVOKED_1_USER_1)
        .user(UserTestUtil.getTraineeUser1())
        .build();
  }

  public static JwtToken getJwtToken2OfTrainee1() {
    return JwtToken.builder()
        .id(TEST_JWT_ID_2_USER_1)
        .token(TEST_JWT_TOKEN_2_USER_1)
        .user(UserTestUtil.getTraineeUser1())
        .build();
  }

  public static JwtToken getJwtToken3OfTrainee1() {
    return JwtToken.builder()
        .id(TEST_JWT_ID_3_USER_1)
        .token(TEST_JWT_TOKEN_3_USER_1)
        .user(UserTestUtil.getTraineeUser1())
        .build();
  }

  public static List<JwtToken> getValidJwtTokensOfTrainee1() {
    return List.of(getJwtToken2OfTrainee1(), getJwtToken3OfTrainee1());
  }
}
