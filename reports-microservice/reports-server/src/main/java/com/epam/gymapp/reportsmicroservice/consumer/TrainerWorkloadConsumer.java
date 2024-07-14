package com.epam.gymapp.reportsmicroservice.consumer;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import jakarta.validation.Valid;

public interface TrainerWorkloadConsumer {

  void updateTrainersWorkload(@Valid TrainerWorkloadUpdateDtoList updateDtoList);
}
