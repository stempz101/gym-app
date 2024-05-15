package com.epam.gymapp.mainmicroservice.bdd.component;

import com.epam.gymapp.mainmicroservice.GymAppApplication;
import com.epam.gymapp.mainmicroservice.bdd.config.CucumberSpringConfiguration;
import com.epam.gymapp.mainmicroservice.repository.TrainingRepository;
import io.cucumber.java.ParameterType;
import io.cucumber.spring.CucumberContextConfiguration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.SpyBean;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = GymAppApplication.class)
public class CucumberComponentTestConfiguration extends CucumberSpringConfiguration {

  @SpyBean
  private TrainingRepository trainingRepository;

  @ParameterType("\\d{4}-\\d{2}-\\d{2}")
  public LocalDate localDate(String date) {
    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
  }
}
