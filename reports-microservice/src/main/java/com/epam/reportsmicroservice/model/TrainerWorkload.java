package com.epam.reportsmicroservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Month;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trainer_workload")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerWorkload {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "is_active", nullable = false)
  private boolean isActive;

  @Column(name = "training_year", nullable = false)
  private Integer year;

  @Column(name = "training_month", nullable = false)
  @Enumerated(EnumType.STRING)
  private Month month;

  @Column(name = "training_duration", nullable = false)
  private Long duration;
}
