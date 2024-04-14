package com.epam.reportsmicroservice.mapper;

import com.epam.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.reportsmicroservice.model.TrainerWorkload;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = TrainerWorkloadMapper.class)
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

  TrainerWorkloadDto toTrainerWorkloadDto(TrainerWorkload trainerWorkload);
}
