
package com.example.application.services.impl;

import com.example.application.entities.FastData;
import com.example.application.entities.MoonPhase;
import com.example.application.enums.FastType;
import com.example.application.enums.Phases;
import com.example.application.enums.Schedule;
import com.example.application.repository.FastDataRepository;
import com.example.application.repository.MoonPhaseRepository;
import com.example.application.util.SchedulerConfig;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TithiFastReminderService {

  private final FastDataRepository fastDataRepository;
  private final WhatsAppMessageSenderServiceImpl whatsAppMessageSenderServiceImpl;
  private final MoonPhaseRepository moonPhaseRepository;

  public TithiFastReminderService(
      FastDataRepository fastDataRepository,
      WhatsAppMessageSenderServiceImpl whatsAppMessageSenderServiceImpl,
      MoonPhaseRepository moonPhaseRepository) {
    this.fastDataRepository = fastDataRepository;
    this.whatsAppMessageSenderServiceImpl = whatsAppMessageSenderServiceImpl;
    this.moonPhaseRepository = moonPhaseRepository;
  }

  @Scheduled(cron = SchedulerConfig.TITHI_FAST_REMINDER_SCHEDULER_CRON)
  public void sendReminder() {

    LocalDate currentDate = LocalDate.now();

    MoonPhase latestMoonPhase = moonPhaseRepository.findFirstByOrderByIdDesc();
    if (Objects.isNull(latestMoonPhase)) return;

    LocalDate fullMoonDate = latestMoonPhase.getFullMoonDate();
    LocalDate newMoonDate = latestMoonPhase.getNewMoonDate();

    int diffOfTodayAndFullMoon = (int) ChronoUnit.DAYS.between(fullMoonDate, currentDate);
    int diffOfTodayAndNewMoon = (int) ChronoUnit.DAYS.between(newMoonDate, currentDate);

    String tithiOfFullMoon = findTithi(diffOfTodayAndFullMoon);
    String tithiOfNewMoon = findTithi(diffOfTodayAndNewMoon);

    List<FastData> fastDetails = new ArrayList<>();
    if (!tithiOfFullMoon.isBlank()) {
      List<Phases> excludedPhases = Arrays.asList(Phases.NONE, Phases.AFTER_NEW_MOON);
      fastDetails = fastDataRepository.findByPhasesNotInAndFastType(excludedPhases, FastType.valueOf(tithiOfFullMoon));
    } else if (!tithiOfNewMoon.isBlank()) {
      List<Phases> excludedPhases = Arrays.asList(Phases.NONE, Phases.AFTER_FULL_MOON);
      fastDetails = fastDataRepository.findByPhasesNotInAndFastType(excludedPhases, FastType.valueOf(tithiOfNewMoon));
    }
    
    LocalDate higherDate = fullMoonDate.isAfter(newMoonDate) ? fullMoonDate : newMoonDate;

    if (!fastDetails.isEmpty()) {
      fastDetails.forEach(fastData -> {
        switch (fastData.getSchedule()) {
          case ONCE:
            if (currentDate.isAfter(higherDate)) {
              updateFastDataToInactive(fastData);
            }
            // whatsAppMessageSenderServiceImpl.sendWhatsAppMessage(currentDate.plusDays(1));
            break;
          case EVERY_MONTH:
            // whatsAppMessageSenderServiceImpl.sendWhatsAppMessage(currentDate.plusDays(1));
            break;
          default:
            Schedule monthSchedule = Schedule.valueOf(latestMoonPhase.getMonth().toUpperCase());
            if(fastData.getSchedule().equals(monthSchedule)){
              if (currentDate.isAfter(higherDate)) {
                updateFastDataToInactive(fastData);
              }
              // whatsAppMessageSenderServiceImpl.sendWhatsAppMessage(currentDate.plusDays(1));
            }
            break;
        }
      });
    }
  }

  private void updateFastDataToInactive(FastData fastData) {
    fastData.setPhases(Phases.NONE);
    fastData.setStatus(false);
    fastDataRepository.save(fastData);
  }

  private String findTithi(int tithiNumber) {
    return switch (tithiNumber) {
      case -1 -> "PRATHAMA";
      case 0 -> "DVITIYA";
      case 1 -> "TRITIYA";
      case 2 -> "CHATURTI";
      case 3 -> "PANCHAMI";
      case 4 -> "SHASHTI";
      case 5 -> "SAPTAMI";
      case 6 -> "ASHTAMI";
      case 7 -> "NAVAMI";
      case 8 -> "DASHAMI";
      case 9 -> "EKADASHI";
      case 10 -> "DVADASHI";
      case 11 -> "TRAYODASHI";
      case 12 -> "CHATURDASHI";
      default -> "";
    };
  }

}
