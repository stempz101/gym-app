package com.epam.gymapp.mainmicroservice.bdd.integration.steps;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.epam.gymapp.mainmicroservice.bdd.integration.contex.CucumberIntegrationContext;
import com.epam.gymapp.mainmicroservice.dto.TraineeTrainersUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeUpdateDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

public class TraineeSteps {

  @Autowired
  private CucumberIntegrationContext context;

  private TraineeUpdateDto traineeUpdateDto;
  private TraineeTrainersUpdateDto traineeTrainersUpdateDto;

  @Given("the trainee data for update as below:")
  public void the_trainee_data_for_update_as_below(Map<String, String> map) {
    String username = map.get("username");
    String firstName = map.get("firstName");
    String lastName = map.get("lastName");
    String address = map.get("address");

    LocalDate dateOfBirth = map.get("dateOfBirth") != null
        ? LocalDate.parse(map.get("dateOfBirth"), DateTimeFormatter.ISO_LOCAL_DATE)
        : null;
    Boolean isActive = map.get("isActive") != null
        ? Boolean.parseBoolean(map.get("isActive"))
        : null;

    traineeUpdateDto = new TraineeUpdateDto(username, firstName, lastName, dateOfBirth, address, isActive);

    context.setBody(traineeUpdateDto);
  }

  @Given("the trainee username {string} with trainer usernames as below:")
  public void the_trainee_username_with_trainer_usernames_as_below(String traineeUsername, List<String> trainerUsernames) {
    traineeUsername = traineeUsername.equals("NULL") ? null : traineeUsername;

    traineeTrainersUpdateDto = new TraineeTrainersUpdateDto(traineeUsername, trainerUsernames);

    context.setBody(traineeTrainersUpdateDto);
  }

  @Then("the response data should include the updated trainee information")
  public void the_response_data_should_include_the_updated_trainee_information() throws Exception {
    context.getResult().andExpectAll(
        jsonPath("$.username").value(traineeUpdateDto.getUsername()),
        jsonPath("$.firstName").value(traineeUpdateDto.getFirstName()),
        jsonPath("$.lastName").value(traineeUpdateDto.getLastName()),
        jsonPath("$.dateOfBirth").value(traineeUpdateDto.getDateOfBirth()
            .format(DateTimeFormatter.ISO_LOCAL_DATE)),
        jsonPath("$.address").value(traineeUpdateDto.getAddress()),
        jsonPath("$.active").value(traineeUpdateDto.getIsActive())
    );
  }

  @Then("the response data should include the trainee's updated trainer with the same number of trainers specified")
  public void the_response_data_should_include_the_trainee_s_updated_trainer_with_the_same_number_of_trainers_specified() throws Exception {
    ResultActions result = context.getResult();

    result.andExpect(jsonPath("$")
        .value(hasSize(traineeTrainersUpdateDto.getTrainerUsernames().size())));
    for (int i = 0; i < traineeTrainersUpdateDto.getTrainerUsernames().size(); i++) {
      result.andExpect(jsonPath(String.format("$[%d].username", i))
          .value(traineeTrainersUpdateDto.getTrainerUsernames().get(i)));
    }
  }
}
