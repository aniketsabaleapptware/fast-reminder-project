package com.example.application.services;

import java.time.LocalDate;

public interface TwilioMessageSenderService {

  void sendNotification(LocalDate notificationDate);
}
