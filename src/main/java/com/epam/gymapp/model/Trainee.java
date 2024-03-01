package com.epam.gymapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trainee")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "address")
  private String address;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @ManyToMany(cascade = {
      CascadeType.DETACH, CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH
  })
  @JoinTable(
      name = "trainee_trainer",
      joinColumns = @JoinColumn(name = "trainee_id"),
      inverseJoinColumns = @JoinColumn(name = "trainer_id")
  )
  private List<Trainer> trainers;

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Trainee trainee = (Trainee) object;
    return Objects.equals(id, trainee.id) && Objects.equals(dateOfBirth,
        trainee.dateOfBirth) && Objects.equals(address, trainee.address)
        && Objects.equals(user, trainee.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dateOfBirth, address, user);
  }

  @Override
  public String toString() {
    return "Trainee(" +
        "id=" + id +
        ", dateOfBirth=" + dateOfBirth +
        ", address=" + address +
        ", user=" + user +
        ')';
  }
}
