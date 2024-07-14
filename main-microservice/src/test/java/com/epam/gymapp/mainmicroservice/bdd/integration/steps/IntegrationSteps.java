package com.epam.gymapp.mainmicroservice.bdd.integration.steps;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.gymapp.mainmicroservice.bdd.integration.contex.CucumberIntegrationContext;
import com.epam.gymapp.mainmicroservice.dto.ChangePasswordDto;
import com.epam.gymapp.mainmicroservice.dto.JwtDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerCreateDto;
import com.epam.gymapp.mainmicroservice.dto.UserCredentialsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class IntegrationSteps {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CucumberIntegrationContext context;

  @Autowired
  private JmsTemplate jmsTemplate;

  private Scenario scenario;

  @Before
  public void setUp(Scenario scenario) {
    this.scenario = scenario;
  }

  @Given("the user with credentials as below:")
  public void the_user_with_credentials_as_below(Map<String, String> map) {
    if (scenario.getSourceTagNames().contains("@skipAuthentication")) {
      return;
    }

    String username = map.get("username");
    String password = map.get("password");

    UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder()
        .username(username)
        .password(password != null ? password.toCharArray() : null)
        .build();

    context.setBody(userCredentialsDto);
  }

  @Given("the trainee data for creation as below:")
  public void the_trainee_data_for_creation_as_below(Map<String, String> map) {
    String firstName = map.get("firstName");
    String lastName = map.get("lastName");
    String address = map.get("address");

    LocalDate dateOfBirth = map.get("dateOfBirth") != null
        ? LocalDate.parse(map.get("dateOfBirth"), DateTimeFormatter.ISO_LOCAL_DATE)
        : null;

    TraineeCreateDto traineeCreateDto = new TraineeCreateDto(firstName, lastName, dateOfBirth, address);

    context.setBody(traineeCreateDto);
  }

  @Given("the trainer data for creation as below:")
  public void the_trainer_data_for_creation_as_below(Map<String, String> map) {
    String firstName = map.get("firstName");
    String lastName = map.get("lastName");
    String specialization = map.get("specialization");

    TrainerCreateDto trainerCreateDto = new TrainerCreateDto(firstName, lastName, specialization);

    context.setBody(trainerCreateDto);
  }

  @Given("the user with password reset data as below:")
  public void the_user_with_password_reset_data_as_below(Map<String, String> map) {
    String username = map.get("username");
    String oldPassword = map.get("password");
    String newPassword = map.get("newPassword");

    ChangePasswordDto changePasswordDto = ChangePasswordDto.builder()
        .username(username)
        .oldPassword(oldPassword != null ? oldPassword.toCharArray() : null)
        .newPassword(newPassword != null ? newPassword.toCharArray() : null)
        .build();

    context.setBody(changePasswordDto);
  }

  @When("the user calls end point {string} with method as 'GET'")
  public void the_user_calls_end_point_with_method_as_GET(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(get(endPoint));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls end point {string} with method as 'POST'")
  public void the_user_calls_end_point_with_method_as_post(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(post(endPoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(context.getBody()));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls end point {string} with method as 'PUT'")
  public void the_user_calls_end_point_with_method_as_put(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(put(endPoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(context.getBody()));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls end point {string} with method as 'PATCH'")
  public void the_user_calls_end_point_with_method_as_patch(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(patch(endPoint)
        .contentType(MediaType.APPLICATION_JSON)
        .content(context.getBody()));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls end point {string} with method as 'DELETE'")
  public void the_user_calls_end_point_with_method_as_delete(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(delete(endPoint));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls end point in order to authenticate")
  public void the_user_calls_end_point_in_order_to_authenticate() throws Exception {
    if (scenario.getSourceTagNames().contains("@skipAuthentication")) {
      return;
    }

    ResultActions result = mockMvc.perform(post("/api/auth/authenticate")
        .contentType(MediaType.APPLICATION_JSON)
        .content(context.getBody()));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls authorized end point {string} with method as 'GET'")
  public void the_user_calls_authorized_end_point_with_method_as_get(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(get(endPoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + context.getToken()));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls authorized end point {string} with method as 'GET' with following parameters:")
  public void the_user_calls_authorized_end_point_with_method_as_get_with_following_parameters(
      String endPoint, Map<String, String> params) throws Exception {

    MockHttpServletRequestBuilder requestBuilder = get(endPoint);
    params.forEach(requestBuilder::param);
    requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + context.getToken());

    ResultActions result = mockMvc.perform(requestBuilder);
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls authorized end point {string} with method as 'POST'")
  public void the_user_calls_authorized_end_point_with_method_as_post(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(post(endPoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + context.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(context.getBody()));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls authorized end point {string} with method as 'PUT'")
  public void the_user_calls_authorized_end_point_with_method_as_put(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(put(endPoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + context.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(context.getBody()));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls authorized end point {string} with method as 'PATCH'")
  public void the_user_calls_authorized_end_point_with_method_as_patch(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(patch(endPoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + context.getToken())
        .contentType(MediaType.APPLICATION_JSON)
        .content(context.getBody()));
    result.andDo(print());

    context.setResult(result);
  }

  @When("the user calls authorized end point {string} with method as 'DELETE'")
  public void the_user_calls_authorized_end_point_with_method_as_delete(String endPoint) throws Exception {
    ResultActions result = mockMvc.perform(delete(endPoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + context.getToken()));
    result.andDo(print());

    context.setResult(result);
  }

  @Then("the response returned with status code as {int}")
  public void the_response_returned_with_status_code_as(Integer statusCode) throws Exception {
    context.getResult().andExpect(status().is(statusCode));
  }

  @Then("the response data should include a token")
  public void the_response_data_should_include_a_token() throws Exception {
    context.getResult().andExpect(jsonPath("$.token").value(notNullValue()));
  }

  @Then("the response data should include the username {string} after registration")
  public void the_response_data_should_include_the_username_after_registration(String username) throws Exception {
    context.getResult().andExpect(jsonPath("$.username").value(username));
  }

  @Then("the response data should include the password of length {int}")
  public void the_response_data_should_include_the_username_password_of_length(Integer passwordLength) throws Exception {
    context.getResult().andExpect(jsonPath("$.password").value(hasLength(passwordLength)));
  }

  @Then("the authentication response returned with status code as {int}")
  public void the_authentication_response_returned_with_status_code_as(Integer statusCode) throws Exception {
    if (scenario.getSourceTagNames().contains("@skipAuthentication")) {
      return;
    }

    ResultActions result = context.getResult();
    result.andExpect(status().is(statusCode));
    String response = result.andReturn().getResponse().getContentAsString();

    JwtDto jwtDto = objectMapper.readValue(response, JwtDto.class);
    context.setToken(jwtDto.getToken());
  }

  @Then("the response data should include an empty list")
  public void the_response_data_should_include_an_empty_list() throws Exception {
    context.getResult().andExpect(jsonPath("$").value(is(empty())));
  }

  @Then("the response data should include a not empty list")
  public void the_response_data_should_include_a_not_empty_list() throws Exception {
    context.getResult().andExpect(jsonPath("$").value(is(not(empty()))));
  }

  @Then("the response data should include the first name {string} and last name {string}")
  public void the_response_data_should_include_the_first_name_and_last_name(String firstName, String lastName) throws Exception {
    context.getResult().andExpectAll(
        jsonPath("$.firstName").value(firstName),
        jsonPath("$.lastName").value(lastName)
    );
  }
}
