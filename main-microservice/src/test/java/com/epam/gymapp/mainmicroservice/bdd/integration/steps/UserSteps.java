package com.epam.gymapp.mainmicroservice.bdd.integration.steps;

import com.epam.gymapp.mainmicroservice.bdd.integration.contex.CucumberIntegrationContext;
import com.epam.gymapp.mainmicroservice.dto.UserActivateDto;
import io.cucumber.java.en.Given;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSteps {

  @Autowired
  private CucumberIntegrationContext context;

  @Given("the user activation data as below:")
  public void the_user_activation_data_as_below(Map<String, String> map) {
    String username = map.get("username");
    Boolean isActive = map.get("isActive") != null
        ? Boolean.parseBoolean(map.get("isActive"))
        : null;

    UserActivateDto userActivateDto = new UserActivateDto(username, isActive);

    context.setBody(userActivateDto);
  }
}
