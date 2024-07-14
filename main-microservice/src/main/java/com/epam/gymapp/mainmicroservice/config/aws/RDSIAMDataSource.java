package com.epam.gymapp.mainmicroservice.config.aws;

import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;
import lombok.Setter;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.RdsUtilities;
import software.amazon.awssdk.services.rds.model.GenerateAuthenticationTokenRequest;

@Setter
public class RDSIAMDataSource extends HikariDataSource {

  private RdsClient rdsClient;

  @Override
  public String getPassword() {
    RdsUtilities rdsUtilities = rdsClient.utilities();

    URI jdbcUri = parseJdbcURL(getJdbcUrl());

    GenerateAuthenticationTokenRequest request = GenerateAuthenticationTokenRequest.builder()
        .username(getUsername())
        .hostname(jdbcUri.getHost())
        .port(jdbcUri.getPort())
        .build();

    return rdsUtilities.generateAuthenticationToken(request);
  }

  private URI parseJdbcURL(String jdbcUrl) {
    String uri = jdbcUrl.substring(5);
    return URI.create(uri);
  }
}
