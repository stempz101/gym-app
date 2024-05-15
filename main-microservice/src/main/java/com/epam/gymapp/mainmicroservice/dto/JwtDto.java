package com.epam.gymapp.mainmicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
