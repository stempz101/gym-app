package com.epam.gymapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Arrays;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "_user")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private char[] password;

  @Column(name = "is_active", nullable = false)
  private boolean isActive;

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    User user = (User) object;
    return isActive == user.isActive && Objects.equals(id, user.id)
        && Objects.equals(firstName, user.firstName) && Objects.equals(lastName,
        user.lastName) && Objects.equals(username, user.username)
        && Arrays.equals(password, user.password);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id, firstName, lastName, username, isActive);
    result = 31 * result + Arrays.hashCode(password);
    return result;
  }

  @Override
  public String toString() {
    return "User(" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", username='" + username + '\'' +
        ", password=" + password +
        ", isActive=" + isActive +
        ')';
  }
}
