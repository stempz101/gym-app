package com.epam.gymapp.controller.utils;

import com.epam.gymapp.exception.ParsingException;
import java.time.LocalDate;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
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

  public LocalDate parseStringToLocalDate(String date) {
    try {
      return date != null ? LocalDate.parse(date) : null;
    } catch (Exception ex) {
      log.error("mapStringToLocalDate: {}", ex.getMessage(), ex);
      throw new ParsingException(String.format("Cannot parse String '%s' to LocalDate", date));
    }
  }
}
