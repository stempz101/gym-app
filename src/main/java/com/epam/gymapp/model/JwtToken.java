package com.epam.gymapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jwt_token")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "token")
  private String token;

  @Column(name = "is_revoked")
  private boolean isRevoked;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JwtToken jwtToken = (JwtToken) object;
    return isRevoked == jwtToken.isRevoked && Objects.equals(id, jwtToken.id)
        && Objects.equals(token, jwtToken.token) && Objects.equals(user,
        jwtToken.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, token, isRevoked, user);
  }
}
