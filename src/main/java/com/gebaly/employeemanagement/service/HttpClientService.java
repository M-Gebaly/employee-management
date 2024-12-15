package com.gebaly.employeemanagement.service;

import com.gebaly.employeemanagement.exception.ApiException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HttpClientService {
    private final HttpClient httpClient;

    public HttpClientService()  {
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> sendGetRequest(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> sendPostRequest(String uri, String payload) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .uri(URI.create(uri))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void sendEmailNotification (String uri, String emailContent, HttpHeaders headers) throws
            ApiException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .POST(HttpRequest.BodyPublishers.ofString(emailContent))
                    .headers("Content-Type", headers.getContentType().toString())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != HttpStatus.OK.value()) {
                throw new ApiException("Failed to send email", new Throwable(response.body()));
            }
        } catch (Exception e) {
            throw new ApiException("Error occurred while trying to send email", e);
        }
    }
}
