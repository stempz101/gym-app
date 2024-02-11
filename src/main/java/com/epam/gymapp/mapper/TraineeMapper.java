package com.epam.gymapp.mapper;

import com.epam.gymapp.dto.TraineeCreateDto;
import com.epam.gymapp.model.Trainee;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TraineeMapper {

  Trainee toTrainee(TraineeCreateDto traineeCreateDto);
}
