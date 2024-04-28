package com.epam.gymapp.mainmicroservice.mapper;

import com.epam.gymapp.mainmicroservice.dto.TraineeShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerUpdateDto;
import com.epam.gymapp.mainmicroservice.model.Trainee;
import com.epam.gymapp.mainmicroservice.model.Trainer;
import com.epam.gymapp.mainmicroservice.model.Training;
import com.epam.gymapp.mainmicroservice.model.custom.TrainerWorkload;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto.ActionType;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = TraineeMapper.class)
public interface TrainerMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "firstName", target = "user.firstName")
  @Mapping(source = "lastName", target = "user.lastName")
  @Mapping(source = "specialization", target = "specialization.name")
  Trainer toTrainer(TrainerCreateDto trainerCreateDto);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  @Mapping(source = "specialization.name", target = "specialization")
  @Mapping(source = "user.active", target = "isActive")
  @Mapping(source = "trainees", target = "trainees", qualifiedByName = "toTraineeShortInfoDto")
  TrainerInfoDto toTrainerInfoDto(Trainer trainer);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  @Mapping(source = "specialization.name", target = "specialization")
  @Mapping(source = "user.active", target = "isActive")
  @Mapping(source = "trainees", target = "trainees", qualifiedByName = "toTraineeShortInfoDto")
  TrainerInfoDto toTrainerInfoDtoAfterUpdate(Trainer trainer);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  @Named("toTraineeShortInfoDto")
  TraineeShortInfoDto toTraineeShortInfoDto(Trainee trainee);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  @Mapping(source = "specialization.name", target = "specialization")
  TrainerShortInfoDto toTrainerShortInfoDto(Trainer trainer);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "trainer.user.username", target = "username")
  @Mapping(source = "trainer.user.firstName", target = "firstName")
  @Mapping(source = "trainer.user.lastName", target = "lastName")
  @Mapping(source = "trainer.user.active", target = "isActive")
  @Mapping(source = "date", target = "trainingDate")
  @Mapping(source = "duration", target = "trainingDuration")
  TrainerWorkloadUpdateDto toTrainerWorkloadUpdateDto(Training training);

  default TrainerWorkloadUpdateDto toTrainerWorkloadUpdateDto(Training training, ActionType actionType) {
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = toTrainerWorkloadUpdateDto(training);
    trainerWorkloadUpdateDto.setActionType(actionType);
    return trainerWorkloadUpdateDto;
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "username", target = "username")
  @Mapping(source = "firstName", target = "firstName")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "isActive", target = "isActive")
  @Mapping(target = "trainingDate", expression = """
      java(java.time.LocalDate.of(trainerWorkload.getTrainingYear(), trainerWorkload.getTrainingMonth(), 1))
      """)
  @Mapping(source = "trainingDuration", target = "trainingDuration")
  TrainerWorkloadUpdateDto toTrainerWorkloadUpdateDto(TrainerWorkload trainerWorkload);

  default TrainerWorkloadUpdateDto toTrainerWorkloadUpdateDto(TrainerWorkload trainerWorkload, ActionType actionType) {
    TrainerWorkloadUpdateDto trainerWorkloadUpdateDto = toTrainerWorkloadUpdateDto(trainerWorkload);
    trainerWorkloadUpdateDto.setActionType(actionType);
    return trainerWorkloadUpdateDto;
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "firstName", target = "user.firstName")
  @Mapping(source = "lastName", target = "user.lastName")
  @Mapping(source = "isActive", target = "user.active")
  void updateTrainer(TrainerUpdateDto trainerUpdateDto, @MappingTarget Trainer trainer);
}
