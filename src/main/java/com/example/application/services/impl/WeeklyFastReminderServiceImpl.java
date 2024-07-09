package com.example.application.services.impl;


import com.example.application.entities.FastData;
import com.example.application.enums.FastType;
import com.example.application.repository.FastDataRepository;
import com.example.application.services.WeeklyFastReminderService;
import com.example.application.util.SchedulerConfig;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WeeklyFastReminderServiceImpl implements WeeklyFastReminderService {

  private final FastDataRepository fastDataRepository;

  private final WhatsAppMessageSenderServiceImpl whatsAppMessageSenderServiceImpl;

  public WeeklyFastReminderServiceImpl(
      FastDataRepository fastDataRepository,
      WhatsAppMessageSenderServiceImpl whatsAppMessageSenderServiceImpl) {
    this.fastDataRepository = fastDataRepository;
    this.whatsAppMessageSenderServiceImpl = whatsAppMessageSenderServiceImpl;
  }

  @Override
  @Scheduled(cron = SchedulerConfig.WEEKLY_FAST_REMINDER_SCHEDULER_CRON)
  @SneakyThrows
  public void sendReminder() {
    List<FastData> fastdays = fastDataRepository.findByStatusTrueAndFastType(FastType.WEEKLY);
    for (FastData fastData : fastdays) {
      switch (fastData.getSchedule()) {
        case ONCE:
          System.out.println("Weekly fast reminder sent! --ONCE");
          System.out.println("WhatsApp message sent! ---------->");
          System.out.println("Username: " + fastData.getUser().getUsername());
          System.out.println("Fast Day: " + fastData.getDay());
          System.out.println("Fast Type: " + fastData.getFastType());
          System.out.println("Phases: " + fastData.getPhases());
          System.out.println("Schedule: " + fastData.getSchedule());
          System.out.println();
//          whatsAppMessageSenderServiceImpl.sendWhatsAppMessage(tomorrowsDate);
          fastData.setStatus(false);
          fastDataRepository.save(fastData);
          break;
        case EVERY_WEEK:
          System.out.println("Weekly fast reminder sent! --EVERY_WEEK");
          System.out.println("WhatsApp message sent! ---------->");
          System.out.println("Username: " + fastData.getUser().getUsername());
          System.out.println("Fast Day: " + fastData.getDay());
          System.out.println("Fast Type: " + fastData.getFastType());
          System.out.println("Phases: " + fastData.getPhases());
          System.out.println("Schedule: " + fastData.getSchedule());
//          whatsAppMessageSenderServiceImpl.sendWhatsAppMessage(tomorrowsDate);
          break;
        default:
          break;
      }

    }
  }

//  private void sendNotification(WeeklyFast fastday) {
//    System.out.println("Reminder: Tomorrow you have a fast, It's " + fastday.getDay() + "!");
//  }
}
