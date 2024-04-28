package com.epam.gymapp.mainmicroservice.mapper;

import com.epam.gymapp.mainmicroservice.dto.TrainingCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainingInfoDto;
import com.epam.gymapp.mainmicroservice.model.Training;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TrainingMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "name", target = "name")
  @Mapping(source = "date", target = "date")
  @Mapping(source = "duration", target = "duration")
  Training toTraining(TrainingCreateDto trainingCreateDto);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "name", target = "name")
  @Mapping(source = "date", target = "date")
  @Mapping(source = "type.name", target = "type")
  @Mapping(source = "duration", target = "duration")
  @Mapping(target = "traineeName", expression = """
      java(String.format("%s %s", training.getTrainee().getUser().getFirstName(),
        training.getTrainee().getUser().getLastName()))""")
  @Mapping(target = "trainerName", expression = """
      java(String.format("%s %s", training.getTrainer().getUser().getFirstName(),
        training.getTrainer().getUser().getLastName()))""")
  TrainingInfoDto toTrainingInfoDto(Training training);
}
