package com.epam.gymapp.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, char[]> {

  @Override
  public boolean isValid(char[] value, ConstraintValidatorContext context) {
    return value != null && value.length != 0;
  }
}
