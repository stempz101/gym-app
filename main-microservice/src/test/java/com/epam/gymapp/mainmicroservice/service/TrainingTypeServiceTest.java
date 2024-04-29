package com.epam.gymapp.mainmicroservice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.gymapp.mainmicroservice.model.TrainingType;
import com.epam.gymapp.mainmicroservice.repository.TrainingTypeRepository;
import com.epam.gymapp.mainmicroservice.test.utils.TrainingTypeTestUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

  @InjectMocks
  private TrainingTypeService trainingTypeService;

  @Mock
  private TrainingTypeRepository trainingTypeRepository;

  @Test
  void selectTrainingTypes_Success() {
    // Given
    List<TrainingType> expectedResult = TrainingTypeTestUtil.getTrainingTypes();

    // When
    when(trainingTypeRepository.findAll()).thenReturn(expectedResult);

    List<TrainingType> result = trainingTypeService.selectTrainingTypes();

    // Then
    verify(trainingTypeRepository, times(1)).findAll();

    assertThat(result, samePropertyValuesAs(expectedResult));
  }
}
