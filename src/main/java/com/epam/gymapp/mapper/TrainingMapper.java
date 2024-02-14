package com.epam.gymapp.mapper;

import com.epam.gymapp.dto.TrainingCreateDto;
import com.epam.gymapp.model.Training;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TrainingMapper {

  Training toTraining(TrainingCreateDto trainingCreateDto);
}
