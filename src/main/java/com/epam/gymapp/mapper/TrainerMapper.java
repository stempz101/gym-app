package com.epam.gymapp.mapper;

import com.epam.gymapp.dto.TraineeShortInfoDto;
import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.dto.TrainerInfoDto;
import com.epam.gymapp.dto.TrainerShortInfoDto;
import com.epam.gymapp.dto.TrainerUpdateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.model.Trainee;
import com.epam.gymapp.model.Trainer;
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
  @Mapping(source = "user.username", target = "username")
  @Mapping(source = "user.password", target = "password")
  UserCredentialsDto toUserCredentialsDto(Trainer trainer);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(source = "user.firstName", target = "firstName")
  @Mapping(source = "user.lastName", target = "lastName")
  @Mapping(source = "specialization.name", target = "specialization")
  @Mapping(source = "trainees", target = "trainees", qualifiedByName = "toTraineeShortInfoDto")
  TrainerInfoDto toTrainerInfoDto(Trainer trainer);

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
  @Mapping(source = "firstName", target = "user.firstName")
  @Mapping(source = "lastName", target = "user.lastName")
  void updateTrainer(TrainerUpdateDto trainerUpdateDto, @MappingTarget Trainer trainer);
}
