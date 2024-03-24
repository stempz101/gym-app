package com.epam.gymapp.filter;

import com.epam.gymapp.model.JwtToken;
import com.epam.gymapp.repository.JwtTokenRepository;
import com.epam.gymapp.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final static Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final static String JWT_EXPIRED_MESSAGE = "JWT Token has expired";

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  private final JwtTokenRepository jwtTokenRepository;

  private final HandlerExceptionResolver handlerExceptionResolver;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    try {
      log.debug("Extracting and validating JWT Token");
      jwtService.extractBearerToken(request).ifPresent(token -> {
        String username = jwtService.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          boolean tokenIsRevoked = jwtTokenRepository.findByToken(token)
              .map(JwtToken::isRevoked)
              .orElse(false);
          if (userDetails != null && !tokenIsRevoked && jwtService.isTokenValid(token, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
          }
        }
      });

      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException ex) {
      log.error("JWT Token has expired", ex);
      handlerExceptionResolver.resolveException(request, response, null, ex);
    }
  }
}
