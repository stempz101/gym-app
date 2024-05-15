package com.epam.gymapp.reportsmicroservice.service;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDtoList;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.mapper.TrainerWorkloadMapper;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.repository.TrainerWorkloadRepository;
import com.epam.gymapp.reportsmicroservice.repository.custom.CustomTrainerWorkloadRepository;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

  private final static Logger log = LoggerFactory.getLogger(TrainerWorkloadService.class);

  private final TrainerWorkloadRepository trainerWorkloadRepository;
  private final CustomTrainerWorkloadRepository customTrainerWorkloadRepository;

  private final TrainerWorkloadMapper trainerWorkloadMapper;

  public TrainerWorkloadDtoList retrieveTrainersWorkloadForMonth(
      int year, int month, String firstName, String lastName) {
    log.info("Selecting trainers' workload for certain month: year={}, month={}, firstName={}, lastName={}",
        year, Month.of(month), firstName, lastName);

    List<TrainerWorkloadDto> workloadList = customTrainerWorkloadRepository
        .findAllByYearAndMonthAndFirstNameAndLastName(year, Month.of(month), firstName, lastName)
        .stream()
        .flatMap(trainerWorkload -> trainerWorkload.getYears().entrySet().stream()
            .filter(yearEntry -> yearEntry.getKey() == year)
            .flatMap(yearEntry -> yearEntry.getValue().entrySet().stream()
                .filter(monthEntry -> monthEntry.getKey().equals(Month.of(month)))
                .map(monthEntry -> trainerWorkloadMapper
                    .toTrainerWorkloadDto(trainerWorkload, yearEntry.getKey(),
                        monthEntry.getKey(), monthEntry.getValue()))
            )
        ).toList();

    return new TrainerWorkloadDtoList(workloadList);
  }

  @Transactional("mongoTransactionManager")
  public void updateTrainersWorkload(TrainerWorkloadUpdateDtoList updateDtoList) {
    log.info("Updating trainers' workload: {}", updateDtoList);

    updateDtoList.getItems().forEach(this::updateTrainerWorkload);

    log.info("Trainers' workload was successfully updated");
  }

  private void updateTrainerWorkload(TrainerWorkloadUpdateDto updateDto) {
    if (updateDto.getActionType().equals(ActionType.ADD)) {
      addTrainingDuration(updateDto);
    } else {
      subtractTrainingDuration(updateDto);
    }
  }

  private void addTrainingDuration(TrainerWorkloadUpdateDto updateDto) {
    log.info("Adding training duration (duration={}) to the trainer record (username={})",
        updateDto.getTrainingDuration(), updateDto.getUsername());

    TrainerWorkload trainerWorkload = trainerWorkloadRepository
        .findById(updateDto.getUsername())
        .orElseGet(() -> trainerWorkloadMapper.toTrainerWorkload(updateDto));

    int year = updateDto.getTrainingDate().getYear();
    Month month = updateDto.getTrainingDate().getMonth();
    long trainingDuration = updateDto.getTrainingDuration();

    Map<Integer, Map<Month, Long>> years = trainerWorkload.getYears();
    Map<Month, Long> months = years.computeIfAbsent(year, key -> new HashMap<>());
    months.put(month, months.getOrDefault(month, 0L) + trainingDuration);

    trainerWorkloadRepository.save(trainerWorkload);
  }

  private void subtractTrainingDuration(TrainerWorkloadUpdateDto updateDto) {
    log.info("Subtracting training duration (duration={}) from the trainer record (username={})",
        updateDto.getTrainingDuration(), updateDto.getUsername());

    trainerWorkloadRepository.findById(updateDto.getUsername()).ifPresent(trainerWorkload -> {
      int year = updateDto.getTrainingDate().getYear();
      Month month = updateDto.getTrainingDate().getMonth();
      long trainingDuration = updateDto.getTrainingDuration();

      Map<Integer, Map<Month, Long>> years = trainerWorkload.getYears();
      years.computeIfPresent(year, (yearKey, months) -> {
        months.computeIfPresent(month, (monthKey, workloadTrainingDuration) -> {
          long updatedTrainingDuration = workloadTrainingDuration - trainingDuration;
          return updatedTrainingDuration > 0 ? updatedTrainingDuration : null;
        });

        return months.isEmpty() ? null : months;
      });

      if (years.isEmpty()) {
        trainerWorkloadRepository.deleteById(trainerWorkload.getUsername());
      } else {
        trainerWorkloadRepository.save(trainerWorkload);
      }
    });
  }
}
