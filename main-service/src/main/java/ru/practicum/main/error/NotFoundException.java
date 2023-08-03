package ru.practicum.main.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String error) {
        super(error);
    }

    public NotFoundException(String error, Throwable cause) {
        super(error, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    protected NotFoundException(String error, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(error, cause, enableSuppression, writableStackTrace);
    }
}
