package com.epam.gymapp.dto;

import com.epam.gymapp.dto.validation.Password;
import com.epam.gymapp.dto.validation.PasswordsNotEqual;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordsNotEqual
public class ChangePasswordDto {

  @NotBlank(message = "{validation.user.not-blank.username}")
  private String username;

  @Password(message = "{validation.user.password.old-password}")
  private char[] oldPassword;

  @Password(message = "{validation.user.password.new-password}")
  private char[] newPassword;

  @Override
  public String toString() {
    return "ChangePasswordDto{" +
        "username='" + username + '\'' +
        ", oldPassword=" + oldPassword +
        ", newPassword=" + newPassword +
        '}';
  }
}
