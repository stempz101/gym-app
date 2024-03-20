package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TrainingTypeController;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.model.TrainingType;
import com.epam.gymapp.service.TrainingTypeService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainingTypeControllerImpl implements TrainingTypeController {

  private final TrainingTypeService trainingTypeService;
  private final JwtProcess jwtProcess;

  @Override
  public List<TrainingType> selectTrainingTypes(HttpServletRequest request) {
    jwtProcess.processToken(request);
    return trainingTypeService.selectTrainingTypes();
  }
}
