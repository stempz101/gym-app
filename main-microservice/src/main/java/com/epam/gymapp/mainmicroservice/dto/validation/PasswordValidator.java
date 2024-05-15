package com.epam.gymapp.mainmicroservice.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, char[]> {

  @Override
  public boolean isValid(char[] value, ConstraintValidatorContext context) {
    return value != null && value.length != 0;
  }
}
