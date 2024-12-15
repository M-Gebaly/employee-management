package com.gebaly.employeemanagement.service;

import com.gebaly.employeemanagement.exception.ApiException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpResponse;

@Service
public class EmailValidationService {

    private final HttpClientService httpClientService;

    public EmailValidationService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }


    public String validateEmail(String email) {
        try {
            HttpResponse<String> response = httpClientService.sendGetRequest("https://api.mailgun.net/v4/address/validate?address=" + email);
            // Parse response and return result
            return parseResponse(response.body());
        } catch (IOException | InterruptedException e) {
            throw new ApiException("Error when validating email", e);
        }
    }

    private String parseResponse(String responseBody) {
        return null;
    }
}
