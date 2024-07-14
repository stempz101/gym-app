package com.epam.gymapp.mainmicroservice.config.aws;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.rds.RdsClient;

@Configuration
@Profile({"stg", "prod"})
public class RDSConfiguration {

  @Bean
  public DataSource awsDataSource(
      RdsClient rdsClient,
      @Value("${spring.datasource.url}") String jdbcUrl,
      @Value("${spring.datasource.username}") String username,
      @Value("${spring.datasource.hikari.max-lifetime}") long maxLifetime
  ) {
    RDSIAMDataSource dataSource = new RDSIAMDataSource();

    dataSource.setRdsClient(rdsClient);
    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setMaxLifetime(maxLifetime);

    return dataSource;
  }

  @Bean
  public RdsClient rdsClient() {
    return RdsClient.builder().region(new DefaultAwsRegionProviderChain().getRegion()).build();
  }
}
