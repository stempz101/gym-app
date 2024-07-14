package com.epam.gymapp.reportsmicroservice.service.impl;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.service.TrainerWorkloadService;
import com.epam.gymapp.reportsmicroservice.service.strategy.AddTrainingDurationStrategy;
import com.epam.gymapp.reportsmicroservice.service.strategy.SubtractTrainingDurationStrategy;
import com.epam.gymapp.reportsmicroservice.service.strategy.UpdateActionStrategy;

public abstract class AbstractTrainerWorkloadService implements TrainerWorkloadService {

  protected void updateTrainerWorkload(TrainerWorkloadUpdateDto updateDto) {
    UpdateActionStrategy strategy = getUpdateStrategy(updateDto.getActionType());
    TrainerWorkload trainerWorkload = findTrainerWorkload(updateDto);

    strategy.updateTrainingDuration(trainerWorkload, updateDto);

    saveOrDeleteTrainerWorkload(trainerWorkload);
  }

  protected UpdateActionStrategy getUpdateStrategy(TrainerWorkloadUpdateDto.ActionType actionType) {
    if (actionType.equals(TrainerWorkloadUpdateDto.ActionType.ADD)) {
      return new AddTrainingDurationStrategy();
    } else {
      return new SubtractTrainingDurationStrategy();
    }
  }

  protected abstract TrainerWorkload findTrainerWorkload(TrainerWorkloadUpdateDto updateDto);

  protected abstract void saveOrDeleteTrainerWorkload(TrainerWorkload trainerWorkload);
}
