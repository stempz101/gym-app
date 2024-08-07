package com.epam.gymapp.mainmicroservice.mapper;

import com.epam.gymapp.mainmicroservice.dto.TraineeCreateDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeShortInfoDto;
import com.epam.gymapp.mainmicroservice.dto.TraineeUpdateDto;
import com.epam.gymapp.mainmicroservice.dto.TrainerShortInfoDto;
import com.epam.gymapp.mainmicroservice.model.Trainee;
import com.epam.gymapp.mainmicroservice.model.Trainer;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = TrainerMapper.class)
public interface TraineeMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "firstName", target = "user.firstName")
  @Mapping(source = "lastName", target = "user.lastName")
  @Mapping(source = "dateOfBirth", target = "dateOfBirth")
  @Mapping(source = "address", target = "address")
  Trainee toTrainee(TraineeCreateDto traineeCreateDto);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  @Mapping(source = "dateOfBirth", target = "dateOfBirth")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "user.active", target = "isActive")
  @Mapping(source = "trainers", target = "trainers", qualifiedByName = "toTrainerShortInfoDto")
  TraineeInfoDto toTraineeInfoDto(Trainee trainee);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  @Mapping(source = "dateOfBirth", target = "dateOfBirth")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "user.active", target = "isActive")
  @Mapping(source = "trainers", target = "trainers", qualifiedByName = "toTrainerShortInfoDto")
  TraineeInfoDto toTraineeInfoDtoAfterUpdate(Trainee trainee);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  @Mapping(source = "specialization.name", target = "specialization")
  @Named("toTrainerShortInfoDto")
  TrainerShortInfoDto toTrainerShortInfoDto(Trainer trainer);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  TraineeShortInfoDto toTraineeShortInfoDto(Trainee trainee);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "firstName", target = "user.firstName")
  @Mapping(source = "lastName", target = "user.lastName")
  @Mapping(source = "dateOfBirth", target = "dateOfBirth")
  @Mapping(source = "address", target = "address")
  @Mapping(source = "isActive", target = "user.active")
  void updateTrainee(TraineeUpdateDto traineeUpdateDto, @MappingTarget Trainee trainee);
}
