package com.epam.gymapp.config;

import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = "com.epam.gymapp")
@PropertySource("classpath:application.properties")
public class GymApplicationConfiguration {

  @Bean
  public static PlaceholderConfigurerSupport placeholderConfigurerSupport() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
