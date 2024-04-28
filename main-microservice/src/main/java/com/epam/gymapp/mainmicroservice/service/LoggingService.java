package com.epam.gymapp.mainmicroservice.service;

import com.epam.gymapp.mainmicroservice.controller.utils.ControllerUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {

  private static final Logger log = LoggerFactory.getLogger(LoggingService.class);

  public void logRequest(HttpServletRequest request, Object body) {
    StringBuilder builder = new StringBuilder();
    Map<String, List<String>> params = ControllerUtils.getRequestParams(request);

    builder.append("REST call received - ");
    builder.append("method=").append(request.getMethod()).append(" ");
    builder.append("path='").append(request.getRequestURI()).append("' ");
    builder.append("headers=").append(Collections.list(request.getHeaderNames())).append(" ");

    if (!params.isEmpty()) {
      builder.append("parameters=").append(params).append(" ");
    }

    if (body != null) {
      builder.append("body=").append(body);
    }

    log.info(builder.toString());
  }

  public void logResponse(HttpServletRequest request, HttpServletResponse response, Object body) {
    StringBuilder builder = new StringBuilder();
    int responseStatus = response.getStatus();

    builder.append(responseStatus < 400 ? "REST call completed - " : "REST call failed - ");
    builder.append("method=").append(request.getMethod()).append(" ");
    builder.append("status=").append(responseStatus).append(" ");
    builder.append("path='").append(request.getRequestURI()).append("' ");

    if (!response.getHeaderNames().isEmpty()) {
      builder.append("responseHeaders=").append(response.getHeaderNames()).append(" ");
    }

    if (body != null) {
      builder.append("responseBody=").append(body);
    }

    log.info(builder.toString());
  }
}
