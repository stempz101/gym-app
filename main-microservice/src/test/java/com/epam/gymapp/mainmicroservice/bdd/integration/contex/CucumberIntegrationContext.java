package com.epam.gymapp.mainmicroservice.bdd.integration.contex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultActions;

@Data
@Component
@Scope("cucumber-glue")
public class CucumberIntegrationContext {

  @Autowired
  private ObjectMapper objectMapper;

  private String body;
  private String token;
  private ResultActions result;

  private Object messageBodyForBroker;
  private UUID correlationId;

  public void setBody(Object body) {
    try {
      this.body = objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
