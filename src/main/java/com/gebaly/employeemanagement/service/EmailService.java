package com.gebaly.employeemanagement.service;

import com.gebaly.employeemanagement.exception.ApiException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Service
public class EmailService {

    private static final String SENDGRID_API_URL = "https://api.sendgrid.com/v3/mail/send";

    private final HttpClientService httpClientService;
    private final String sendGridApiKey;

    public EmailService(HttpClientService httpClientService, @Value("${sendgrid.api.key}") String apiKey) {
        this.httpClientService = httpClientService;
        this.sendGridApiKey = apiKey;
    }

    @Retryable(value = { ApiException.class }, maxAttempts = 5, backoff = @Backoff(delay = 2000, multiplier = 2))
    public void sendEmail(String to, String subject, String body) {
        JSONObject email = createEmailJson(to, subject, body);
        HttpHeaders headers = createHeaders();

        int retries = 3;
        while (retries-- > 0) {
            try {
                httpClientService.sendEmailNotification(SENDGRID_API_URL, email.toString(), headers);
                break;
            } catch (ApiException e) {
                if (retries == 0) {
                    fallbackEmail(to, subject, body);
                }
            }
        }
    }

    private JSONObject createEmailJson(String to, String subject, String body) {
        JSONObject email = new JSONObject();
        email.put("personalizations", new JSONArray().put(new JSONObject().put("to", new JSONArray().put(new JSONObject().put("email", to)))));
        email.put("from", new JSONObject().put("email", "email@example.com"));
        email.put("subject", subject);
        email.put("content", new JSONArray().put(new JSONObject().put("type", "text/plain").put("value", body)));
        return email;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + sendGridApiKey);
        return headers;
    }

    private void fallbackEmail(String to, String subject, String body) {
        try {
            JSONObject email = createEmailJson(to, subject, body);
            HttpHeaders headers = createHeaders();
            httpClientService.sendEmailNotification(SENDGRID_API_URL, email.toString(), headers);
        } catch(Exception e) {
            System.out.println("Failed to send fallback email");
        }
    }
}
