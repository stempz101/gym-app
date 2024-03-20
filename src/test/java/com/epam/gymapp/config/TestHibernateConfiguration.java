package com.epam.gymapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.epam.gymapp.repository")
public class TestHibernateConfiguration {

}
