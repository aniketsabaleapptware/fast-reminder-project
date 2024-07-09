package com.example.application.entities;


import com.example.application.enums.FastType;
import com.example.application.enums.Phases;
import com.example.application.enums.Schedule;
import com.example.application.enums.WeekDays;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
public class FastData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private LocalDate createdAt = LocalDate.now();

  @Enumerated(EnumType.STRING)
  @Column(name = "fast_day")
  private WeekDays day = WeekDays.NONE;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Schedule schedule;

  private boolean status = true;

  @Enumerated(EnumType.STRING)
  @Column(name = "fast_type", nullable = false)
  private FastType fastType;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private Phases phases = Phases.NONE;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

}
