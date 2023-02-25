package com.springservice.app.notes.exceptions;

import lombok.ToString;

@ToString
public class NotNullableException extends RuntimeException {
    public NotNullableException(String message) {
        super(message);
    }
}
