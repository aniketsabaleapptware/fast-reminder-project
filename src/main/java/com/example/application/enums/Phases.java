package com.example.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Phases {

  NONE("None"),
  BOTH("Both"),
  AFTER_FULL_MOON("After Full Moon"),
  AFTER_NEW_MOON("After New Moon");

  private final String displayName;

  public static Phases fromDisplayName(String displayName) {
    for (Phases phase : Phases.values()) {
      if (phase.getDisplayName().equalsIgnoreCase(displayName)) {
        return phase;
      }
    }
    throw new IllegalArgumentException("No enum constant for display value: " + displayName);
  }
}
