package com.epam.gymapp.mainmicroservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionUser implements Serializable {

  private String username;
  private LocalDateTime createdAt;

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SessionUser that = (SessionUser) object;
    return Objects.equals(username, that.username) && Objects.equals(createdAt,
        that.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, createdAt);
  }
}
