package com.epam.gymapp.dto;

import com.epam.gymapp.dto.validation.Password;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Optional;
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

  @JsonProperty(access = Access.READ_ONLY)
  private String token;

  @Override
  public String toString() {
    return "UserCredentialsDto{" +
        "username='" + username + '\'' +
        ", password=" + password +
        ", token=" + (token != null ? token.toCharArray() : null) +
        '}';
  }
}
