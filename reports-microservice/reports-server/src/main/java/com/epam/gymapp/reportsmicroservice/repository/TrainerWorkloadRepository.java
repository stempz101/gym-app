package com.epam.gymapp.reportsmicroservice.repository;

import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerWorkloadRepository extends CrudRepository<TrainerWorkload, Long> {

  @Query("""
      select tw from TrainerWorkload tw
      where tw.year = :year and tw.month = :month
      and (tw.username = :username or :username is null)
      """)
  List<TrainerWorkload> findAllByYearAndMonthAndUsername(int year, Month month, String username);

  Optional<TrainerWorkload> findByUsernameAndYearAndMonth(String username, int year, Month month);
}
