package com.epam.gymapp.reportsmicroservice.model;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearSummary {

  protected int year;
  protected List<MonthSummary> months;

  @DynamoDbAttribute("year")
  public int getYear() {
    return year;
  }

  @DynamoDbAttribute("months")
  public List<MonthSummary> getMonths() {
    return months;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    YearSummary that = (YearSummary) object;
    return year == that.year && Objects.equals(months, that.months);
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, months);
  }
}
