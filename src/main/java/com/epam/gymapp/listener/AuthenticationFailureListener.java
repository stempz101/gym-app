package com.epam.gymapp.listener;

import com.epam.gymapp.service.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureListener implements
    ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  private final HttpServletRequest httpServletRequest;
  private final LoginAttemptService loginAttemptService;

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
    String xfHeader = httpServletRequest.getHeader("X-Forwarded-For");
    if (xfHeader == null || xfHeader.isEmpty() ||
        !xfHeader.contains(httpServletRequest.getRemoteAddr())) {
      loginAttemptService.loginFailed(httpServletRequest.getRemoteAddr());
    } else {
      loginAttemptService.loginFailed(xfHeader.split(",")[0]);
    }
  }
}
