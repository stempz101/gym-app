package com.epam.gymapp.validator;

import com.epam.gymapp.dto.ChangePasswordDto;
import com.epam.gymapp.dto.UserActivateDto;
import com.epam.gymapp.dto.UserCredentialsDto;
import com.epam.gymapp.exception.UserValidationException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserValidator implements GeneralValidator {

  private static final Logger log = LoggerFactory.getLogger(UserValidator.class);

  public void validate(UserCredentialsDto userCredentialsDto) {
    log.debug("Validating user credentials: {}", userCredentialsDto);

    validateObjectNotNull(userCredentialsDto,
        () -> new IllegalArgumentException("UserCredentialsDto must not be null"));
    validateStringNotBlank(userCredentialsDto.getUsername(),
        () -> new UserValidationException("Username must be specified"));
    validateCharArray(userCredentialsDto.getPassword(),
        () -> new UserValidationException("Password must be specified"));
  }

  public void validate(ChangePasswordDto changePasswordDto) {
    log.debug("Validating change password data: {}", changePasswordDto);

    validateObjectNotNull(changePasswordDto,
        () -> new IllegalArgumentException("ChangePasswordDto must not be null"));
    validateStringNotBlank(changePasswordDto.getUsername(),
        () -> new UserValidationException("Username must be specified"));
    validateCharArray(changePasswordDto.getOldPassword(),
        () -> new UserValidationException("Current password must be specified"));
    validateCharArray(changePasswordDto.getNewPassword(),
        () -> new UserValidationException("New password must be specified"));
    validatePasswordsOnEquality(changePasswordDto.getOldPassword(),
        changePasswordDto.getNewPassword());
  }

  public void validate(UserActivateDto userActivateDto) {
    log.debug("Validating user activation: {}", userActivateDto);

    validateObjectNotNull(userActivateDto,
        () -> new IllegalArgumentException("UserActivateDto must not be null"));
    validateStringNotBlank(userActivateDto.getUsername(),
        () -> new UserValidationException("Username must be specified"));
    validateObjectNotNull(userActivateDto.getIsActive(),
        () -> new UserValidationException("Active status must be specified"));
  }

  private void validatePasswordsOnEquality(char[] oldPassword, char[] newPassword) {
    if (Arrays.equals(oldPassword, newPassword)) {
      throw new UserValidationException("New password must not be equal to the current one");
    }
  }
}
