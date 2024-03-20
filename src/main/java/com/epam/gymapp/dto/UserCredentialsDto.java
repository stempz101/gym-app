package com.epam.gymapp.dto;

import com.epam.gymapp.dto.validation.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsDto {

  @NotBlank(message = "{validation.user.not-blank.username}")
  private String username;

  @Password
  private char[] password;

  @Override
  public String toString() {
    return "UserCredentialsDto{" +
        "username='" + username + '\'' +
        ", password=" + password +
        '}';
  }
}
