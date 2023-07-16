package ru.practicum.server.error;

public class ErrorResponse {
    private int statusCode;
    private String error;

    public ErrorResponse() {
    }

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.error = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String message) {
        this.error = message;

    }
}
