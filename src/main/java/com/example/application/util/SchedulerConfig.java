package com.example.application.util;

import org.springframework.stereotype.Component;

@Component
public class SchedulerConfig {

  public static final String MOON_PHASE_SCHEDULER_CRON = "0 38 13 * * *";
  public static final String TITHI_FAST_REMINDER_SCHEDULER_CRON = "0 46 05 * * *";
  public static final String WEEKLY_FAST_REMINDER_SCHEDULER_CRON = "0 12 12 * * *";

}
