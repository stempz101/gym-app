package com.epam.gymapp.validator;

import com.epam.gymapp.model.TrainingType;
import java.util.function.Supplier;

public interface GeneralValidator {

  default <X extends RuntimeException> void validateObjectNotNull(
      Object object, Supplier<X> exceptionSupplier
  ) {
    if (object == null) {
      throw exceptionSupplier.get();
    }
  }

  default <X extends RuntimeException> void validateStringNotBlank(
      String str, Supplier<X> exceptionSupplier
  ) {
    if (str == null || str.isBlank()) {
      throw exceptionSupplier.get();
    }
  }

  default <X extends RuntimeException> void validateTrainingType(
      TrainingType trainingType, Supplier<X> exceptionSupplier
  ) {
    if (trainingType == null || trainingType.getName() == null
        || trainingType.getName().isBlank()) {
      throw exceptionSupplier.get();
    }
  }
}
