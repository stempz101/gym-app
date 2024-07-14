package com.epam.gymapp.reportsmicroservice.service.strategy;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.model.MonthSummary;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.model.YearSummary;
import java.time.Month;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubtractTrainingDurationStrategy implements UpdateActionStrategy {

  private static final Logger log = LoggerFactory.getLogger(SubtractTrainingDurationStrategy.class);

  @Override
  public void updateTrainingDuration(TrainerWorkload trainerWorkload,
      TrainerWorkloadUpdateDto updateDto) {
    log.info("Subtracting training duration (duration={}) from the trainer record (username={})",
        updateDto.getTrainingDuration(), updateDto.getUsername());

    int year = updateDto.getTrainingDate().getYear();
    Month month = updateDto.getTrainingDate().getMonth();
    long trainingDuration = updateDto.getTrainingDuration();

    YearSummary yearSummary = trainerWorkload.getYears().stream()
        .filter(ys -> ys.getYear() == year)
        .findFirst()
        .orElse(null);

    if (yearSummary != null) {
      MonthSummary monthSummary = yearSummary.getMonths().stream()
          .filter(ms -> ms.getMonth() == month.getValue())
          .findFirst()
          .orElse(null);

      if (monthSummary != null) {
        monthSummary.setDuration(Math.max(0, monthSummary.getDuration() - trainingDuration));

        if (monthSummary.getDuration() == 0) {
          yearSummary.getMonths().remove(monthSummary);
        }

        if (yearSummary.getMonths().isEmpty()) {
          trainerWorkload.getYears().remove(yearSummary);
        }
      }
    }
  }
}
