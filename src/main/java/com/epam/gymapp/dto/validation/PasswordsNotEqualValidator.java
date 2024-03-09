package com.epam.gymapp.dto.validation;

import com.epam.gymapp.dto.ChangePasswordDto;
import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsNotEqualValidator implements
    ConstraintValidator<PasswordsNotEqual, ChangePasswordDto> {

  @Override
  public boolean isValid(ChangePasswordDto changePasswordDto,
      ConstraintValidatorContext constraintValidatorContext) {
    char[] oldPassword = changePasswordDto.getOldPassword();
    char[] newPassword = changePasswordDto.getNewPassword();

    if (isBlank(oldPassword) || isBlank(newPassword)) {
      return true;
    }

    return !Arrays.equals(oldPassword, newPassword);
  }

  private boolean isBlank(char[] password) {
    return password == null || password.length == 0;
  }
}
