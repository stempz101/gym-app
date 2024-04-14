package com.epam.gymapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trainer")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {
      CascadeType.DETACH, CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH
  })
  @JoinColumn(name = "training_type_id", referencedColumnName = "id", nullable = false)
  private TrainingType specialization;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @ManyToMany(cascade = {
      CascadeType.DETACH, CascadeType.MERGE,
      CascadeType.PERSIST, CascadeType.REFRESH
  })
  @JoinTable(
      name = "trainee_trainer",
      joinColumns = @JoinColumn(name = "trainer_id"),
      inverseJoinColumns = @JoinColumn(name = "trainee_id")
  )
  private List<Trainee> trainees;

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Trainer trainer = (Trainer) object;
    return Objects.equals(id, trainer.id) && Objects.equals(specialization,
        trainer.specialization) && Objects.equals(user, trainer.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, specialization, user);
  }

  @Override
  public String toString() {
    return "Trainer(" +
        "id=" + id +
        ", specialization=" + specialization +
        ", user=" + user +
        ')';
  }
}
