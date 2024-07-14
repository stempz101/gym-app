package com.epam.gymapp.reportsmicroservice.service.strategy;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;

public interface UpdateActionStrategy {
  void updateTrainingDuration(TrainerWorkload trainerWorkload, TrainerWorkloadUpdateDto updateDto);
}
