package com.springservice.app.notes.exceptions;

import lombok.ToString;

@ToString
public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
