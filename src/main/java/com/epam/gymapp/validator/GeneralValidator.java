package com.epam.gymapp.validator;

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

  default <X extends RuntimeException> void validateCharArray(
      char[] array, Supplier<X> exceptionSupplier
  ) {
    if (array == null || array.length == 0) {
      throw exceptionSupplier.get();
    }
  }
}
