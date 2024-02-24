package com.epam.gymapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordDto {

  private String username;
  private char[] oldPassword;
  private char[] newPassword;
}
