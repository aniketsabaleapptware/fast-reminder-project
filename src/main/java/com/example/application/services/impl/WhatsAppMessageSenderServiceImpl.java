package com.example.application.services.impl;

import com.example.application.services.WhatsAppMessageSenderService;
import java.time.LocalDate;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppMessageSenderServiceImpl implements WhatsAppMessageSenderService {


  @Value("${whatsapp.api.url}")
  private String apiUrl;

  @Value("${whatsapp.access.token}")
  private String accessToken;
   private static final String RECIPIENT_WA_Number = "7038745987";


  @Override
  @SneakyThrows
  public void sendWhatsAppMessage(LocalDate notificationDate) {
    OkHttpClient client = new OkHttpClient();

    String jsonPayload = "{\n" +
        "    \"messaging_product\": \"whatsapp\",\n" +
        "    \"to\": \"" + "91" + RECIPIENT_WA_Number + "\",\n" +
        "    \"type\": \"template\",\n" +
        "    \"template\": {\n" +
        "        \"name\": \"hello_world\",\n" +
        "        \"language\": {\n" +
        "            \"code\": \"en_US\"\n" +
        "        }\n" +
        "    }\n" +
        "}";

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, jsonPayload);

    Request request = new Request.Builder()
        .url(apiUrl)
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer " + accessToken)
        .build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Whats app message not sent");
      }
    } catch (RuntimeException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

}
