package com.epam.gymapp.mainmicroservice.interceptor;

import com.epam.gymapp.mainmicroservice.service.LoggingService;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

  private final LoggingService loggingService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
        && request.getMethod().equals(HttpMethod.GET.name())) {
      loggingService.logRequest(request, null);
    }

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
}
