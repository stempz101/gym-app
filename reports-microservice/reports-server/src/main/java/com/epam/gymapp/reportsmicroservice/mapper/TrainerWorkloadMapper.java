package com.epam.gymapp.reportsmicroservice.mapper;

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
  @Mapping(target = "status", expression = "java(trainerWorkloadUpdateDto.getIsActive().toString())")
  TrainerWorkload toTrainerWorkload(TrainerWorkloadUpdateDto trainerWorkloadUpdateDto);
}
