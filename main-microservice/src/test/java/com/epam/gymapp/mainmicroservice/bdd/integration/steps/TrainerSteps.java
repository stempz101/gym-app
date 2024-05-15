package com.epam.gymapp.mainmicroservice.bdd.integration.steps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.epam.gymapp.mainmicroservice.bdd.integration.contex.CucumberIntegrationContext;
import com.epam.gymapp.mainmicroservice.dto.TrainerInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

  @Given("the predefined trainer's workload data for a given month as below:")
  public void the_predefined_trainer_s_workload_data_for_a_given_month_as_below(Map<String, String> map) throws Exception {
    String username = map.get("username");
    int year = Integer.parseInt(map.get("year"));
    Month month = Month.valueOf(map.get("month"));
    long duration = Long.parseLong(map.get("duration"));

    ResultActions result = mockMvc.perform(get("/api/trainers/" + username)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + context.getToken()));
    TrainerInfoDto trainerInfoDto = objectMapper
        .readValue(result.andReturn().getResponse().getContentAsString(), TrainerInfoDto.class);

    TrainerWorkloadDto trainerWorkloadDto = TrainerWorkloadDto.builder()
        .username(username)
        .firstName(trainerInfoDto.getFirstName())
        .lastName(trainerInfoDto.getLastName())
        .isActive(trainerInfoDto.isActive())
        .year(year)
        .month(month)
        .duration(duration)
        .build();

    context.setMessageBodyForBroker(new TrainerWorkloadDtoList(List.of(trainerWorkloadDto)));
    context.setCorrelationId(UUID.randomUUID());
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
