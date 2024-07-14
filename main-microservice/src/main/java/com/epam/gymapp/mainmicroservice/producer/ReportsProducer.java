package com.epam.gymapp.mainmicroservice.producer;

import com.epam.gymapp.mainmicroservice.exception.TrainerWorkloadUpdateException;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import java.util.List;

public interface ReportsProducer {

  void updateTrainerWorkload(Training training, ActionType actionType);

  void updateTrainerWorkload(List<TrainerWorkloadUpdateDto> trainerWorkloadUpdateDtos);

  default void updateTrainerWorkloadFailure(Exception ex) {
    throw new TrainerWorkloadUpdateException(ex);
  }
}
