package com.epam.gymapp.reportsmicroservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkload {

  @Id
  protected String username;
  protected String status;
  @Indexed
  protected String firstName;
  @Indexed
  protected String lastName;
  protected List<YearSummary> years;

  @DynamoDbPartitionKey
  @DynamoDbAttribute("username")
  public String getUsername() {
    return username;
  }

  @DynamoDbSortKey
  @DynamoDbAttribute("status")
  public String getStatus() {
    return status;
  }

  @DynamoDbSecondaryPartitionKey(indexNames = "TrainerName-Index")
  @DynamoDbAttribute("firstName")
  public String getFirstName() {
    return firstName;
  }

  @DynamoDbSecondarySortKey(indexNames = "TrainerName-Index")
  @DynamoDbAttribute("lastName")
  public String getLastName() {
    return lastName;
  }

  @DynamoDbAttribute("years")
  public List<YearSummary> getYears() {
    if (years == null) {
      years = new ArrayList<>();
    }
    return years;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TrainerWorkload that = (TrainerWorkload) object;
    return Objects.equals(username, that.username) && Objects.equals(status,
        that.status) && Objects.equals(firstName, that.firstName)
        && Objects.equals(lastName, that.lastName) && Objects.equals(years,
        that.years);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, status, firstName, lastName, years);
  }
}
