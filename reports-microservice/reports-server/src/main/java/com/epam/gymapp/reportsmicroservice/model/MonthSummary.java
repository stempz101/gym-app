package com.epam.gymapp.reportsmicroservice.model;

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
public class MonthSummary {

  protected int month;
  protected long duration;

  @DynamoDbAttribute("month")
  public int getMonth() {
    return month;
  }

  @DynamoDbAttribute("duration")
  public long getDuration() {
    return duration;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    MonthSummary that = (MonthSummary) object;
    return month == that.month && duration == that.duration;
  }

  @Override
  public int hashCode() {
    return Objects.hash(month, duration);
  }
}
