package com.epam.gymapp.reportsmicroservice.service;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;

public interface TrainerWorkloadService {

  void updateTrainersWorkload(TrainerWorkloadUpdateDtoList updateDtoList);
}
