package com.example.application.repository;

import com.example.application.entities.MoonPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoonPhaseRepository extends JpaRepository<MoonPhase, Integer> {
  MoonPhase findFirstByOrderByIdDesc();

}
