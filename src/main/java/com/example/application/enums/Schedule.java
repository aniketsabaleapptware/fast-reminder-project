package com.example.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Schedule {
  ONCE("Once"),
  EVERY_WEEK("Every Week"),
  EVERY_MONTH("Every Month"),

  JANUARY("January"),
  FEBRUARY("February"),
  MARCH("March"),
  APRIL("April"),
  MAY("May"),
  JUNE("June"),
  JULY("July"),
  AUGUST("August"),
  SEPTEMBER("September"),
  OCTOBER("October"),
  NOVEMBER("November"),
  DECEMBER("December");

  private final String displayName;

  public static Schedule fromDisplayName(String displayValue) {
    for (Schedule schedule : Schedule.values()) {
      if (schedule.displayName.equals(displayValue)) {
        return schedule;
      }
    }
    throw new IllegalArgumentException("No enum constant for display value: " + displayValue);
  }
}
