package com.epam.gymapp.reportsmicroservice.mapper;

import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadDto;
import com.epam.gymapp.reportsmicroservice.dto.TrainerWorkloadUpdateDto;
import com.epam.gymapp.reportsmicroservice.model.TrainerWorkload;
import java.time.Month;
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
  TrainerWorkload toTrainerWorkload(TrainerWorkloadUpdateDto trainerWorkloadUpdateDto);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "username", target = "username")
  @Mapping(source = "firstName", target = "firstName")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "active", target = "isActive")
  TrainerWorkloadDto toTrainerWorkloadDto(TrainerWorkload trainerWorkload);

  default TrainerWorkloadDto toTrainerWorkloadDto(TrainerWorkload trainerWorkload,
      int year, Month month, long trainingDuration) {
    TrainerWorkloadDto trainerWorkloadDto = toTrainerWorkloadDto(trainerWorkload);
    trainerWorkloadDto.setYear(year);
    trainerWorkloadDto.setMonth(month);
    trainerWorkloadDto.setDuration(trainingDuration);
    return trainerWorkloadDto;
  }
}
