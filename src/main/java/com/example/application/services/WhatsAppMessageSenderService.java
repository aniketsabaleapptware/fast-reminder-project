package com.example.application.services;

import java.time.LocalDate;

public interface WhatsAppMessageSenderService {

  void sendWhatsAppMessage(LocalDate notificationDate);
}
