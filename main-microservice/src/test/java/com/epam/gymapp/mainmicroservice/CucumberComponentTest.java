package com.epam.gymapp.mainmicroservice;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("feature/component")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.epam.gymapp.mainmicroservice.bdd.component")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,value = "pretty, html:target/cucumber-report/cucumber-component.html")
public class CucumberComponentTest {

}
