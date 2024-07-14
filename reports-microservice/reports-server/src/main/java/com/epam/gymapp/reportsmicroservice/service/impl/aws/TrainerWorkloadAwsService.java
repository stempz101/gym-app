package com.epam.gymapp.reportsmicroservice.service.impl.aws;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.mapper.TrainerWorkloadMapper;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.repository.aws.TrainerWorkloadAwsRepository;
import com.epam.gymapp.reportsmicroservice.service.impl.AbstractTrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"stg", "prod"})
@RequiredArgsConstructor
public class TrainerWorkloadAwsService extends AbstractTrainerWorkloadService {

  private final static Logger log = LoggerFactory.getLogger(TrainerWorkloadAwsService.class);

  private final TrainerWorkloadAwsRepository trainerWorkloadRepository;

  private final TrainerWorkloadMapper trainerWorkloadMapper;

  @Override
  public void updateTrainersWorkload(TrainerWorkloadUpdateDtoList updateDtoList) {
    log.info("Updating trainers' workload: {}", updateDtoList);

    updateDtoList.getItems().forEach(this::updateTrainerWorkload);

    log.info("Trainers' workload was successfully updated");
  }

  @Override
  protected TrainerWorkload findTrainerWorkload(TrainerWorkloadUpdateDto updateDto) {
    return trainerWorkloadRepository
        .findById(updateDto.getUsername(), updateDto.getIsActive().toString())
        .orElseGet(() -> trainerWorkloadMapper.toTrainerWorkload(updateDto));
  }

  @Override
  protected void saveOrDeleteTrainerWorkload(TrainerWorkload trainerWorkload) {
    if (trainerWorkload.getYears().isEmpty()) {
      trainerWorkloadRepository.deleteById(trainerWorkload.getUsername(), trainerWorkload.getStatus());
    } else {
      trainerWorkloadRepository.save(trainerWorkload);
    }
  }
}
