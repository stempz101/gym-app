package com.epam.gymapp.reportsmicroservice.mapper;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TrainerWorkloadMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "username", target = "username")
  @Mapping(source = "firstName", target = "firstName")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "isActive", target = "isActive")
  @Mapping(target = "year", expression = """
      java(trainerWorkloadUpdateDto.getTrainingDate().getYear())
      """)
  @Mapping(target = "month", expression = """
      java(trainerWorkloadUpdateDto.getTrainingDate().getMonth())
      """)
  @Mapping(source = "trainingDuration", target = "duration")
  TrainerWorkload toTrainerWorkload(TrainerWorkloadUpdateDto trainerWorkloadUpdateDto);

  @Mapping(source = "active", target = "isActive")
  TrainerWorkloadDto toTrainerWorkloadDto(TrainerWorkload trainerWorkload);
}
