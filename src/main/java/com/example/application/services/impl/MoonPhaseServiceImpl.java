package com.example.application.services.impl;

import com.example.application.entities.MoonPhase;
import com.example.application.repository.MoonPhaseRepository;
import com.example.application.services.MoonPhaseService;
import com.example.application.util.SchedulerConfig;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MoonPhaseServiceImpl implements MoonPhaseService {

  private final MoonPhaseRepository moonPhaseRepository;

  public MoonPhaseServiceImpl(MoonPhaseRepository moonPhaseRepository) {
    this.moonPhaseRepository = moonPhaseRepository;
  }

  @Override
  @Scheduled(cron = SchedulerConfig.MOON_PHASE_SCHEDULER_CRON)
  public void scheduleMoonPhaseCalculation() {
    LocalDate currentDate = LocalDate.now();
    MoonPhase latestMoonPhase = moonPhaseRepository.findFirstByOrderByIdDesc();
    if (Objects.isNull(latestMoonPhase)) {
      latestMoonPhase = updateMoonPhases(currentDate);
      moonPhaseRepository.save(latestMoonPhase);
    }

    LocalDate fullMoonDate = latestMoonPhase.getFullMoonDate();
    LocalDate newMoonDate = latestMoonPhase.getNewMoonDate();
    LocalDate higherDate = getMostRecentMoonDate(fullMoonDate, newMoonDate);
    LocalDate recalculationTriggerDate = higherDate.plusDays(12);

    currentDate = LocalDate.of(2024, 7, 4);
    if (currentDate.isEqual(recalculationTriggerDate)) {
      updateMoonPhases(currentDate);
    }
  }

  private MoonPhase updateMoonPhases(LocalDate currentDate) {
    LocalDate recalculatedFullMoonDate = findCurrentMonthMoonPhase(currentDate, 14.77);
    LocalDate recalculatedNewMoonDate = findCurrentMonthMoonPhase(currentDate, 0);

    MoonPhase newMoonPhaseEntity = MoonPhase.builder()
        .month(currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase())
        .fullMoonDate(recalculatedFullMoonDate)
        .newMoonDate(recalculatedNewMoonDate)
        .build();

    moonPhaseRepository.save(newMoonPhaseEntity);
    return newMoonPhaseEntity;
  }

  private LocalDate getMostRecentMoonDate(LocalDate fullMoonDate, LocalDate newMoonDate) {
    return fullMoonDate.isAfter(newMoonDate) ? fullMoonDate : newMoonDate;
  }

  private LocalDate findCurrentMonthMoonPhase(LocalDate date, double targetPhase) {
    LocalDate firstDayOfMonth = date.withDayOfMonth(1);
    return findMoonPhase(firstDayOfMonth, targetPhase);
  }

  private LocalDate findMoonPhase(LocalDate startDate, double targetPhase) {
    double daysSinceNewMoon = daysSinceLastNewMoon(startDate);
    double daysToTargetPhase = (targetPhase - daysSinceNewMoon + 29.53) % 29.53;
    return startDate.plusDays((long) Math.ceil(daysToTargetPhase));
  }

  private double daysSinceLastNewMoon(LocalDate date) {
    LocalDate knownNewMoon = LocalDate.of(2000, 1, 6);
    return ChronoUnit.DAYS.between(knownNewMoon, date) % 29.53;
  }
}
