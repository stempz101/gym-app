package com.epam.gymapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  protected String firstName;
  protected String lastName;
  private String username;
  private char[] password;
  private boolean isActive;
}
