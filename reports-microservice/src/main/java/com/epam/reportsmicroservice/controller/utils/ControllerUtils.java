package com.epam.reportsmicroservice.controller.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class ControllerUtils {

  private final Logger log = LoggerFactory.getLogger(ControllerUtils.class);

  public Map<String, List<String>> getRequestParams(HttpServletRequest request) {
    return request.getParameterMap().entrySet().stream()
        .map(entry -> new SimpleEntry<>(entry.getKey(), Arrays.asList(entry.getValue())))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
