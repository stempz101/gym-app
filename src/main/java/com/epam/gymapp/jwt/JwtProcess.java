package com.epam.gymapp.jwt;

import com.epam.gymapp.exception.UnauthorizedException;
import com.epam.gymapp.model.User;
import com.epam.gymapp.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProcess {

  private final JwtService jwtService;
  private final UserService userService;

  public void processToken(HttpServletRequest request) {
    String token = jwtService.extractBearerToken(request)
        .orElseThrow(UnauthorizedException::new);

    String username = jwtService.extractUsername(token);
    User user = userService.getUserByUsername(username)
        .orElseThrow(UnauthorizedException::new);

    if (!jwtService.isTokenValid(token, user)) {
      throw new UnauthorizedException();
    }
  }
}
