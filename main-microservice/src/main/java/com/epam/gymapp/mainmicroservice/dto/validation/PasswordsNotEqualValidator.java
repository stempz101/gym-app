package com.epam.gymapp.mainmicroservice.dto.validation;

import com.epam.gymapp.mainmicroservice.dto.ChangePasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

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
