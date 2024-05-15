package com.epam.gymapp.mainmicroservice.service;

import com.epam.gymapp.mainmicroservice.model.TrainingType;
import com.epam.gymapp.mainmicroservice.repository.TrainingTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {

  private static final Logger log = LoggerFactory.getLogger(TrainingTypeService.class);

  private final TrainingTypeRepository trainingTypeRepository;

  @Transactional(readOnly = true)
  public List<TrainingType> selectTrainingTypes() {
    log.info("Selecting all Training Types");

    return trainingTypeRepository.findAll();
  }
}
