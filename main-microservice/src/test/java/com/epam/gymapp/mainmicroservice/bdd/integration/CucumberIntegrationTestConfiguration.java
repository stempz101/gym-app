package com.epam.gymapp.mainmicroservice.bdd.integration;

import com.epam.gymapp.mainmicroservice.GymAppApplication;
import com.epam.gymapp.mainmicroservice.bdd.config.CucumberSpringConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = GymAppApplication.class)
@AutoConfigureMockMvc
public class CucumberIntegrationTestConfiguration extends CucumberSpringConfiguration {

}
