package com.epam.gymapp.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = "com.epam.gymapp.repository")
public class TestHibernateConfiguration {

}
