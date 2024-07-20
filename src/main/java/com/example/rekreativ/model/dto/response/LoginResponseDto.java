package com.example.rekreativ.model.dto.response;

import org.springframework.http.HttpHeaders;

public class LoginResponseDto {
    private HttpHeaders headers;
    private String token;

    public LoginResponseDto() {
    }

    public LoginResponseDto(HttpHeaders headers, String token) {
        this.headers = headers;
        this.token = token;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
