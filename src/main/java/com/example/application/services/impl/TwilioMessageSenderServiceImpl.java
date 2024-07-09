package com.example.application.services.impl;

import com.example.application.services.TwilioMessageSenderService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioMessageSenderServiceImpl implements TwilioMessageSenderService {

  @Value("${twilio.account.sid}")
  private String accountSid;

  @Value("${twilio.auth.token}")
  private String authToken;

  @Override
  public void sendNotification(LocalDate notificationDate) {
    Twilio.init(accountSid, authToken);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
    String formattedDate = notificationDate.format(formatter);
    String messageBody = "🌺 Reminder: Tomorrow, " + formattedDate + ", is Chaturthi. May the blessings of Lord Ganesha bring you peace, happiness, and prosperity. 🕉️✨ Remember to observe your fast and offer your prayers. 🙏🏼";

    PhoneNumber from = new PhoneNumber("whatsapp:+14155238886");
    PhoneNumber to = new PhoneNumber("whatsapp:+917038745987");

    Message.creator(to, from, messageBody).create();
  }
}
