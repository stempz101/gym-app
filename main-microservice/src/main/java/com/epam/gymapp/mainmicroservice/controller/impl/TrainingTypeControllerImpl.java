package com.epam.gymapp.mainmicroservice.controller.impl;

import com.epam.gymapp.mainmicroservice.controller.TrainingTypeController;
import com.epam.gymapp.mainmicroservice.model.TrainingType;
import com.epam.gymapp.mainmicroservice.service.TrainingTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainingTypeControllerImpl implements TrainingTypeController {

  private static final Logger log = LoggerFactory.getLogger(TrainingTypeControllerImpl.class);

  private final TrainingTypeService trainingTypeService;

  @Override
  public List<TrainingType> selectTrainingTypes() {
    log.info("Starting to select a list of Training Types");
    return trainingTypeService.selectTrainingTypes();
  }
}
