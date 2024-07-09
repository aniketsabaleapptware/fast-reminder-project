package com.example.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FastType {

  PRATHAMA("Prathama"),
  DVITIYA("Dvitiya"),
  TRITIYA("Tritiya"),
  CHATURTI("Chaturti"),
  PANCHAMI("Panchami"),
  SHASHTI("Shashti"),
  SAPTAMI("Saptami"),
  ASHTAMI("Ashtami"),
  NAVAMI("Navami"),
  DASHAMI("Dashami"),
  EKADASHI("Ekadashi"),
  DVADASHI("Dvadashi"),
  TRAYODASHI("Trayodashi"),
  CHATURDASHI("Chaturdashi"),
  WEEKLY("Weekly");

  private String displayName;

  public static FastType fromDisplayName(String displayName) {
    for (FastType type : FastType.values()) {
      if (type.getDisplayName().equals(displayName)) {
        return type;
      }
    }
    throw new IllegalArgumentException("No constant with display name " + displayName);
  }
}
