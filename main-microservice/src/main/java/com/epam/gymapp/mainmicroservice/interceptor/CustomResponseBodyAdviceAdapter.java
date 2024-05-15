package com.epam.gymapp.mainmicroservice.interceptor;

import com.epam.gymapp.mainmicroservice.service.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomResponseBodyAdviceAdapter implements ResponseBodyAdvice<Object> {

  private final LoggingService loggingService;
  private final HttpServletRequest httpServletRequest;

  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {
    return httpServletRequest.getRequestURI().startsWith("/api/");
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (request instanceof ServletServerHttpRequest
        && response instanceof ServletServerHttpResponse) {
      loggingService.logResponse(
          ((ServletServerHttpRequest) request).getServletRequest(),
          ((ServletServerHttpResponse) response).getServletResponse(), body);
    }

    return body;
  }
}
