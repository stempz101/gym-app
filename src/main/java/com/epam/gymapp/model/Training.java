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
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Training {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainee_id", referencedColumnName = "id", nullable = false)
  private Trainee trainee;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_id", referencedColumnName = "id", nullable = false)
  private Trainer trainer;

  @Column(name = "training_name", nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "training_type_id", referencedColumnName = "id", nullable = false)
  private TrainingType type;

  @Column(name = "training_date", nullable = false)
  private LocalDate date;

  @Column(name = "training_duration", nullable = false)
  private int duration;

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    Training training = (Training) object;
    return duration == training.duration && Objects.equals(id, training.id)
        && Objects.equals(trainee, training.trainee) && Objects.equals(trainer,
        training.trainer) && Objects.equals(name, training.name)
        && Objects.equals(type, training.type) && Objects.equals(date,
        training.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, trainee, trainer, name, type, date, duration);
  }

  @Override
  public String toString() {
    return "Training(" +
        "id=" + id +
        ", trainee=" + trainee +
        ", trainer=" + trainer +
        ", name='" + name + '\'' +
        ", type=" + type +
        ", date=" + date +
        ", duration=" + duration +
        ')';
  }
}
