package ru.practicum.main.error;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private int statusCode;
    private String error;

    public ErrorResponse() {
    }

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.error = message;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setError(String message) {
        this.error = message;

    }
}
