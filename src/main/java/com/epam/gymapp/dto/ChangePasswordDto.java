package com.epam.gymapp.dto;

import com.epam.gymapp.dto.validation.Password;
import com.epam.gymapp.dto.validation.PasswordsNotEqual;
import javax.validation.constraints.NotBlank;
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

  @NotBlank(message = "Username must be specified")
  private String username;

  @Password(message = "Current password must be specified")
  private char[] oldPassword;

  @Password(message = "New password must be specified")
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
