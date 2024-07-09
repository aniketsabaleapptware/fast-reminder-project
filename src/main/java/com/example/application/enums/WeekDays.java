package com.example.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeekDays {

  NONE("None"),
  SUNDAY("Sunday"),
  MONDAY("Monday"),
  TUESDAY("Tuesday"),
  WEDNESDAY("Wednesday"),
  THURSDAY("Thursday"),
  FRIDAY("Friday"),
  SATURDAY("Saturday");

  private final String displayName;

  public static  WeekDays fromDisplayName(String displayName) {
    for (WeekDays weekDay : WeekDays.values()) {
      if (weekDay.getDisplayName().equals(displayName)) {
        return weekDay;
      }
    }
    throw new IllegalArgumentException("No constant with display name " + displayName);
  }

}
