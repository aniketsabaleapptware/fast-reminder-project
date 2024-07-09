package com.example.application.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoonPhase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String month;
  private LocalDate fullMoonDate;
  private LocalDate newMoonDate;
}
