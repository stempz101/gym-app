package com.epam.gymapp.bpp;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
import com.epam.gymapp.model.Training;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StorageBeanPostProcessorTest {

  @InjectMocks
  private StorageBeanPostProcessor storageBeanPostProcessor;

  @Spy
  private Map<Long, Trainee> traineeStorage;

  @Spy
  private Map<Long, Trainer> trainerStorage;

  @Spy
  private Map<Long, Training> trainingStorage;

  @Test
  void postProcessAfterInitialization_TraineeStorage_Success() {
    // When
    storageBeanPostProcessor.setTraineeDataPath("data/trainee-data.json");
    storageBeanPostProcessor.postProcessAfterInitialization(traineeStorage, "traineeStorage");

    // Then
    verify(traineeStorage, times(4)).put(anyLong(), any());
  }

  @Test
  void postProcessAfterInitialization_TraineeStorageWrongPath_Success() {
    // When
    storageBeanPostProcessor.setTraineeDataPath("data/foo.json");

    // Then
    assertThrows(RuntimeException.class, () -> storageBeanPostProcessor
        .postProcessAfterInitialization(traineeStorage, "traineeStorage"));
  }

  @Test
  void postProcessAfterInitialization_TrainerStorage_Success() {
    // When
    storageBeanPostProcessor.setTrainerDataPath("data/trainer-data.json");
    storageBeanPostProcessor.postProcessAfterInitialization(trainerStorage, "trainerStorage");

    // Then
    verify(trainerStorage, times(3)).put(anyLong(), any());
  }

  @Test
  void postProcessAfterInitialization_TrainerStorageWrongPath_Success() {
    // When
    storageBeanPostProcessor.setTrainerDataPath("data/foo.json");

    // Then
    assertThrows(RuntimeException.class, () -> storageBeanPostProcessor
        .postProcessAfterInitialization(trainerStorage, "traineeStorage"));
  }

  @Test
  void postProcessAfterInitialization_TrainingStorage_Success() {
    // When
    storageBeanPostProcessor.setTrainingDataPath("data/training-data.json");
    storageBeanPostProcessor.postProcessAfterInitialization(trainingStorage, "trainingStorage");

    // Then
    verify(trainingStorage, times(4)).put(anyLong(), any());
  }

  @Test
  void postProcessAfterInitialization_TrainingStorageWrongPath_Success() {
    // When
    storageBeanPostProcessor.setTrainingDataPath("data/foo.json");

    // Then
    assertThrows(RuntimeException.class, () -> storageBeanPostProcessor
        .postProcessAfterInitialization(trainingStorage, "traineeStorage"));
  }
}
