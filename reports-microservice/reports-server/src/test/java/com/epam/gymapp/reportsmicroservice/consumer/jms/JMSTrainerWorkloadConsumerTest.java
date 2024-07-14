package com.epam.gymapp.reportsmicroservice.consumer.jms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.epam.gymapp.reportsmicroservice.config.TestJmsConfiguration;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDtoList;
import com.epam.gymapp.reportsmicroservice.service.TrainerWorkloadService;
import com.epam.gymapp.reportsmicroservice.test.utils.TrainerWorkloadTestUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

@SpringBootTest
@Import(TestJmsConfiguration.class)
public class JMSTrainerWorkloadConsumerTest {

  @SpyBean
  private JMSTrainerWorkloadConsumer trainerWorkloadConsumer;

  @MockBean
  private TrainerWorkloadService trainerWorkloadService;

  @Autowired
  private JmsTemplate jmsTemplate;

  @Value("${application.messaging.queue.update-trainer-workload}")
  private String updateTrainerWorkloadQueue;

  @Test
  void updateTrainersRecords_Success() throws InterruptedException {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto1(2024, 4, 10, ActionType.ADD);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        TrainerWorkloadTestUtil.getTrainerWorkloadUpdateDto1(2025, 5, 60, ActionType.DELETE);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2));

    // When
    doNothing().when(trainerWorkloadService).updateTrainersWorkload(trainerWorkloadUpdateDtoList);

    sendMessageToUpdateTrainerWorkload(trainerWorkloadUpdateDtoList);

    Thread.sleep(5000);

    // Then
    verify(trainerWorkloadConsumer, times(1))
        .updateTrainersWorkload(any(TrainerWorkloadUpdateDtoList.class));
    verify(trainerWorkloadService, times(1))
        .updateTrainersWorkload(any(TrainerWorkloadUpdateDtoList.class));
  }

  @Test
  void updateTrainersRecords_RequiredFieldsAreInvalid_Failure() throws Exception {
    // Given
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto1 =
        new TrainerWorkloadUpdateDto(null, null, null, null, null, null, null);
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto2 =
        new TrainerWorkloadUpdateDto(null, null, null, null, null, null, null);
    TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList =
        new TrainerWorkloadUpdateDtoList(List.of(trainerWorkloadUpdateDto1, trainerWorkloadUpdateDto2));

    // When
    sendMessageToUpdateTrainerWorkload(trainerWorkloadUpdateDtoList);

    Thread.sleep(5000);

    // Then
    verifyNoInteractions(trainerWorkloadConsumer);
    verifyNoInteractions(trainerWorkloadService);
  }

  private void sendMessageToUpdateTrainerWorkload(TrainerWorkloadUpdateDtoList trainerWorkloadUpdateDtoList) {
    jmsTemplate.convertAndSend(updateTrainerWorkloadQueue, trainerWorkloadUpdateDtoList);
  }
}
