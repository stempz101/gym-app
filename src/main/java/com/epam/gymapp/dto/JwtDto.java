package com.epam.gymapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDto {

  private String token;

  @Override
  public String toString() {
    return "JwtDto{" +
        "token='" + token.toCharArray() + '\'' +
        '}';
  }
}
