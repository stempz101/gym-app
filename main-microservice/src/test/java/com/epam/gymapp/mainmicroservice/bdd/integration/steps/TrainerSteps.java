package com.epam.gymapp.mainmicroservice.bdd.integration.steps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.epam.gymapp.mainmicroservice.bdd.integration.contex.CucumberIntegrationContext;
import com.epam.gymapp.mainmicroservice.dto.TrainerUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class TrainerSteps {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CucumberIntegrationContext context;

  private TrainerUpdateDto trainerUpdateDto;

  @Given("the trainer data for update as below:")
  public void the_trainer_data_for_update_as_below(Map<String, String> map) {
    String username = map.get("username");
    String firstName = map.get("firstName");
    String lastName = map.get("lastName");
    String specialization = map.get("specialization");

    Boolean isActive = map.get("isActive") != null
        ? Boolean.parseBoolean(map.get("isActive"))
        : null;

    trainerUpdateDto = new TrainerUpdateDto(username, firstName, lastName, specialization, isActive);

    context.setBody(trainerUpdateDto);
  }

  @Then("the response data should include the updated trainer information")
  public void the_response_data_should_include_the_updated_trainer_information() throws Exception {
    context.getResult().andExpectAll(
        jsonPath("$.username").value(trainerUpdateDto.getUsername()),
        jsonPath("$.firstName").value(trainerUpdateDto.getFirstName()),
        jsonPath("$.lastName").value(trainerUpdateDto.getLastName()),
        jsonPath("$.specialization").value(notNullValue()),
        jsonPath("$.active").value(trainerUpdateDto.getIsActive())
    );
  }

  @Then("the response data should include the unchanged trainer's specialization")
  public void the_response_data_should_include_the_unchanged_trainer_s_specialization() throws Exception {
    context.getResult().andExpectAll(
        jsonPath("$.specialization").value(is(not(trainerUpdateDto.getSpecialization())))
    );
  }
}
