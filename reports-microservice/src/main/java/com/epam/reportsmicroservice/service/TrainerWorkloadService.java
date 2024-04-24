package com.epam.reportsmicroservice.service;

import com.epam.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.reportsmicroservice.mapper.TrainerWorkloadMapper;
import com.epam.reportsmicroservice.model.TrainerWorkload;
import com.epam.reportsmicroservice.repository.TrainerWorkloadRepository;
import java.time.Month;
import java.util.List;
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

  private final TrainerWorkloadMapper trainerWorkloadMapper;

  @Transactional(readOnly = true)
  public List<TrainerWorkloadDto> retrieveTrainersWorkloadForMonth(int year, int month, String username) {
    log.info("Selecting trainers' workload for certain month: {}, {}", year, Month.of(month));

    return trainerWorkloadRepository.findAllByYearAndMonthAndUsername(year, Month.of(month), username)
        .stream()
        .map(trainerWorkloadMapper::toTrainerWorkloadDto)
        .toList();
  }

  @Transactional
  public void updateTrainersWorkload(List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos) {
    log.info("Updating trainers' workload: {}", trainerWorkloadUpdateDtos);

    trainerWorkloadUpdateDtos.forEach(this::updateTrainerWorkload);

    log.info("Trainers' workload was successfully updated");
  }

  private void updateTrainerWorkload(TrainerWorkloadUpdateDto trainerWorkloadUpdateDto) {
    if (trainerWorkloadUpdateDto.getActionType().equals(ActionType.ADD)) {
      addTrainingDuration(trainerWorkloadUpdateDto);
    } else {
      subtractTrainingDuration(trainerWorkloadUpdateDto);
    }
  }

  private void addTrainingDuration(TrainerWorkloadUpdateDto trainerWorkloadUpdateDto) {
    log.debug("Adding trainer's (username={}) working time is started",
        trainerWorkloadUpdateDto.getUsername());

    String trainerUsername = trainerWorkloadUpdateDto.getUsername();
    int trainingYear = trainerWorkloadUpdateDto.getTrainingDate().getYear();
    Month trainingMonth = trainerWorkloadUpdateDto.getTrainingDate().getMonth();

    trainerWorkloadRepository.findByUsernameAndYearAndMonth(trainerUsername, trainingYear, trainingMonth)
        .ifPresentOrElse(trainerRecord -> addTrainingDurationIfExists(trainerRecord,
                trainerWorkloadUpdateDto),
            () -> saveNewTrainingRecord(trainerWorkloadUpdateDto));
  }

  private void subtractTrainingDuration(TrainerWorkloadUpdateDto trainerWorkloadUpdateDto) {
    log.debug("Subtracting trainer's (username={}) working time is started",
        trainerWorkloadUpdateDto.getUsername());

    String trainerUsername = trainerWorkloadUpdateDto.getUsername();
    int trainingYear = trainerWorkloadUpdateDto.getTrainingDate().getYear();
    Month trainingMonth = trainerWorkloadUpdateDto.getTrainingDate().getMonth();

    trainerWorkloadRepository.findByUsernameAndYearAndMonth(trainerUsername, trainingYear, trainingMonth)
        .ifPresent(trainerRecord -> subtractTrainingDurationIfExists(trainerRecord,
            trainerWorkloadUpdateDto));
  }

  private void addTrainingDurationIfExists(TrainerWorkload trainerWorkload,
      TrainerWorkloadUpdateDto trainerWorkloadUpdateDto) {
    log.debug("Existing workload found. Adding working hours for trainer (username={})",
        trainerWorkload.getUsername());

    trainerWorkload.setDuration(trainerWorkload.getDuration() + trainerWorkloadUpdateDto.getTrainingDuration());
    trainerWorkloadRepository.save(trainerWorkload);
  }

  private void saveNewTrainingRecord(TrainerWorkloadUpdateDto trainerWorkloadUpdateDto) {
    log.debug("Existing workload was not found. Creating new trainer record (username={})",
        trainerWorkloadUpdateDto.getUsername());

    TrainerWorkload trainerWorkload = trainerWorkloadMapper.toTrainerWorkload(trainerWorkloadUpdateDto);
    trainerWorkloadRepository.save(trainerWorkload);
  }

  private void subtractTrainingDurationIfExists(TrainerWorkload trainerWorkload,
      TrainerWorkloadUpdateDto trainerWorkloadUpdateDto) {
    log.debug("Existing workload found. Subtracting working hours for trainer (username={})",
        trainerWorkload.getUsername());

    long updatedDuration = trainerWorkload.getDuration() - trainerWorkloadUpdateDto.getTrainingDuration();
    if (updatedDuration > 0) {
      trainerWorkload.setDuration(updatedDuration);
      trainerWorkloadRepository.save(trainerWorkload);
    } else {
      trainerWorkloadRepository.delete(trainerWorkload);
    }
  }
}
