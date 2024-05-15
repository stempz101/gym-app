package com.epam.gymapp.reportsmicroservice.bdd.component;

import com.epam.gymapp.reportsmicroservice.ReportsMicroserviceApplication;
import com.epam.gymapp.reportsmicroservice.bdd.config.CucumberSpringConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = ReportsMicroserviceApplication.class)
public class CucumberComponentTestConfiguration extends CucumberSpringConfiguration {

}
