package com.epam.gymapp.reportsmicroservice.service.strategy;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.model.MonthSummary;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.model.YearSummary;
import java.time.Month;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddTrainingDurationStrategy implements UpdateActionStrategy {

  private static final Logger log = LoggerFactory.getLogger(AddTrainingDurationStrategy.class);

  @Override
  public void updateTrainingDuration(TrainerWorkload trainerWorkload,
      TrainerWorkloadUpdateDto updateDto) {
    log.info("Adding training duration (duration={}) to the trainer record (username={})",
        updateDto.getTrainingDuration(), updateDto.getUsername());

    int year = updateDto.getTrainingDate().getYear();
    Month month = updateDto.getTrainingDate().getMonth();
    long trainingDuration = updateDto.getTrainingDuration();

    YearSummary yearSummary = trainerWorkload.getYears().stream()
        .filter(ys -> ys.getYear() == year)
        .findFirst()
        .orElseGet(() -> {
          YearSummary newYearSummary = new YearSummary(year, new ArrayList<>());
          trainerWorkload.getYears().add(newYearSummary);
          return newYearSummary;
        });

    MonthSummary monthSummary = yearSummary.getMonths().stream()
        .filter(ms -> ms.getMonth() == month.getValue())
        .findFirst()
        .orElseGet(() -> {
          MonthSummary newMonthSummary = new MonthSummary(month.getValue(), 0);
          yearSummary.getMonths().add(newMonthSummary);
          return newMonthSummary;
        });

    monthSummary.setDuration(monthSummary.getDuration() + trainingDuration);
  }
}
