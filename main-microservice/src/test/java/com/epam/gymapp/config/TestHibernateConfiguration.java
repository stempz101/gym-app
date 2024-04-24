package com.epam.gymapp.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@TestConfiguration
@ComponentScan(basePackages = "com.epam.gymapp.repository",
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.epam.gymapp.repository.redis.*"))
public class TestHibernateConfiguration {

}
