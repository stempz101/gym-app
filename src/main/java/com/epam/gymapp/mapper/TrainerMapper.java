package com.epam.gymapp.mapper;

import com.epam.gymapp.dto.TrainerCreateDto;
import com.epam.gymapp.model.Trainer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TrainerMapper {

  Trainer toTrainer(TrainerCreateDto trainerCreateDto);
}
