package com.epam.gymapp.controller.impl;

import com.epam.gymapp.controller.TrainingController;
import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.dto.TrainingInfoDto;
import com.epam.gymapp.jwt.JwtProcess;
import com.epam.gymapp.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrainingControllerImpl implements TrainingController {

  private final TrainingService trainingService;
  private final JwtProcess jwtProcess;

  @Override
  public void addTraining(TrainingCreateDto trainingCreateDto, HttpServletRequest request) {
    jwtProcess.processToken(request);
    trainingService.addTraining(trainingCreateDto);
  }

  @Override
  public List<TrainingInfoDto> selectTrainings(HttpServletRequest request) {
    jwtProcess.processToken(request);
    return trainingService.selectTrainings();
  }
}
