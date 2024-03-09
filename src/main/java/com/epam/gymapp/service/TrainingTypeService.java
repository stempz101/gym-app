package com.epam.gymapp.service;

import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.repository.TrainingTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {

  private static final Logger log = LoggerFactory.getLogger(TrainingTypeService.class);

  private final TrainingTypeRepository trainingTypeRepository;

  public List<TrainingType> selectTrainingTypes() {
    log.info("Selecting all Training Types");

    return trainingTypeRepository.findAll();
  }
}
