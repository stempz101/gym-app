package com.epam.gymapp.reportsmicroservice.repository.custom;

import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import java.time.Month;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomTrainerWorkloadRepository {

  private final MongoTemplate mongoTemplate;

  public List<TrainerWorkload> findAllByYearAndMonthAndFirstNameAndLastName(
      int year, Month month, String firstName, String lastName) {

    String yearKey = String.format("years.%d", year);
    String monthKey = String.format("%s.%s", yearKey, month);

    Criteria criteria = Criteria.where(yearKey).exists(true)
        .and(monthKey).exists(true);

    if (firstName != null && !firstName.isBlank()) {
      criteria.and("firstName").is(firstName);
    }
    if (lastName != null && !lastName.isBlank()) {
      criteria.and("lastName").is(lastName);
    }

    Query query = new Query().addCriteria(criteria);
    return mongoTemplate.find(query, TrainerWorkload.class);
  }
}
